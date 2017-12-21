package com.luojilab.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.luojilab.router.compiler.utils.Constants;
import com.luojilab.router.compiler.utils.Logger;
import com.luojilab.router.compiler.utils.TypeUtils;
import com.luojilab.router.facade.annotation.Autowired;
import com.luojilab.router.facade.enums.Type;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.luojilab.router.compiler.utils.Constants.ANNOTATION_TYPE_AUTOWIRED;
import static com.luojilab.router.compiler.utils.Constants.KEY_HOST_NAME;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * <p><b>Package:</b> com.luojilab.router.compiler.processor </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> AutowiredProcessor </p>
 * <p><b>Description:</b> Autowired Processor,Only Activity and Fragment/Fragment_V4 allowed </p>
 * Created by leobert on 2017/9/18.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_HOST_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ANNOTATION_TYPE_AUTOWIRED})
public class AutowiredProcessor extends AbstractProcessor {
    private static final String TAG = AutowiredProcessor.class.getSimpleName();

    private Logger logger;

    private Filer mFiler;
    private Types types;
    private TypeUtils typeUtils;
    private Elements elements;

    /**
     * Contain field need autowired and his super class.
     */
    private Map<TypeElement, List<Element>> parentAndChild = new HashMap<>();

    private static final ClassName AndroidLog = ClassName.get("android.util", "Log");

    private static final ClassName NullPointerException = ClassName.get("java.lang", "NullPointerException");

    private static final String SUFFIX_AUTOWIRED = "$$Router$$Autowired";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnv.getFiler();                  // Generate class.
        types = processingEnv.getTypeUtils();            // Get type utils.
        elements = processingEnv.getElementUtils();      // Get class meta.

        typeUtils = new TypeUtils(types, elements);

        logger = new Logger(processingEnv.getMessager());   // Package the log utils.

        logger.info(">>> AutowiredProcessor init. <<<");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            try {
                logger.info(">>> Found autowired field, start... <<<");
                categories(roundEnvironment.getElementsAnnotatedWith(Autowired.class));
                generateHelper();

            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }

        return false;
    }

    private void generateHelper() throws IOException, IllegalAccessException {
        TypeElement type_ISyringe = elements.getTypeElement(Constants.ISYRINGE);

        TypeElement type_JsonService = elements.getTypeElement(Constants.JSON_SERVICE);

        TypeMirror activityTm = elements.getTypeElement(Constants.ACTIVITY).asType();
        TypeMirror fragmentTm = elements.getTypeElement(Constants.FRAGMENT).asType();
        TypeMirror fragmentTmV4 = elements.getTypeElement(Constants.FRAGMENT_V4).asType();

        // Build input param name.
        ParameterSpec objectParamSpec = ParameterSpec.builder(TypeName.OBJECT, "target").build();

        if (MapUtils.isNotEmpty(parentAndChild)) {
            for (Map.Entry<TypeElement, List<Element>> entry : parentAndChild.entrySet()) {
                // Build method : 'inject'
                MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                        .addAnnotation(Override.class)
                        .addModifiers(PUBLIC)
                        .addParameter(objectParamSpec);

                TypeElement parent = entry.getKey();
                List<Element> childs = entry.getValue();

                String qualifiedName = parent.getQualifiedName().toString();
                String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));

                String fileName = parent.getSimpleName() + SUFFIX_AUTOWIRED;

                logger.info(">>> Start process " + childs.size() + " field in " + parent.getSimpleName() + " ... <<<");

                TypeSpec.Builder helper = TypeSpec.classBuilder(fileName)
                        .addJavadoc("Auto generated by " + TAG)
                        .addSuperinterface(ClassName.get(type_ISyringe))
                        .addModifiers(PUBLIC);

                FieldSpec jsonServiceField = FieldSpec.builder(TypeName.get(type_JsonService.asType()),
                        "jsonService", Modifier.PRIVATE).build();
                helper.addField(jsonServiceField);

                logger.info("======== inject jsonservice");

                injectMethodBuilder.addStatement("jsonService = $T.Factory.getInstance().create()",
                        ClassName.get(type_JsonService));

                injectMethodBuilder.addStatement("$T substitute = ($T)target", ClassName.get(parent), ClassName.get(parent));

                // Generate method body, start inject.
                for (Element element : childs) {
                    Autowired fieldConfig = element.getAnnotation(Autowired.class);
                    String fieldName = element.getSimpleName().toString();
                    String originalValue = "substitute." + fieldName;
                    String statment = "substitute." + fieldName + " = substitute.";
                    boolean isActivity = false;
                    if (types.isSubtype(parent.asType(), activityTm)) {  // Activity, then use getIntent()
                        isActivity = true;
                        statment += "getIntent().";
                    } else if (types.isSubtype(parent.asType(), fragmentTm) ||
                            types.isSubtype(parent.asType(), fragmentTmV4)) {   // Fragment, then use getArguments()
                        statment += "getArguments().";
                    } else {
                        throw new IllegalAccessException("The field [" + fieldName + "] need " +
                                "autowired from intent, its parent must be activity or fragment!");
                    }

                    statment = buildStatement(originalValue, statment, typeUtils.typeExchange(element), isActivity);
                    if (statment.startsWith("jsonService.")) {   // Not mortals
                        injectMethodBuilder.beginControlFlow("if (null != jsonService)");
                        injectMethodBuilder.addStatement(
                                "substitute." + fieldName + " = " + statment,
                                (StringUtils.isEmpty(fieldConfig.name()) ? fieldName : fieldConfig.name()),
                                ClassName.get(element.asType())
                        );
                        injectMethodBuilder.nextControlFlow("else");
                        injectMethodBuilder.addStatement(
                                "$T.e(\"" + TAG + "\", \"You want automatic inject the field '"
                                        + fieldName + "' in class '$T' ," +
                                        " but JsonService not found in Router\")", AndroidLog, ClassName.get(parent));

                        injectMethodBuilder.endControlFlow();
                    } else {
                        injectMethodBuilder.addStatement(statment, StringUtils.isEmpty(fieldConfig.name()) ? fieldName : fieldConfig.name());
                    }

                    // Validator
                    if (fieldConfig.required() && !element.asType().getKind().isPrimitive()) {  // Primitive wont be check.
                        injectMethodBuilder.beginControlFlow("if (null == substitute." + fieldName + ")");
                        injectMethodBuilder.addStatement(
                                "$T.e(\"" + TAG + "\", \"The field '" + fieldName + "' is null," + "field description is:" + fieldConfig.desc() +
                                        ",in class '\" + $T.class.getName() + \"!\")", AndroidLog, ClassName.get(parent));

                        if (fieldConfig.throwOnNull()) {
                            injectMethodBuilder.addStatement("throw new $T(" +
                                    "\"The field '" + fieldName + "' is null," + "field description is:" + fieldConfig.desc() +
                                    ",in class '\" + $T.class.getName() + \"!\")", NullPointerException, ClassName.get(parent));
                        }

                        injectMethodBuilder.endControlFlow();
                    }
                }

                helper.addMethod(injectMethodBuilder.build());

                // Generate autowire helper
                JavaFile.builder(packageName, helper.build()).build().writeTo(mFiler);

                logger.info(">>> " + parent.getSimpleName() + " has been processed, " + fileName + " has been generated. <<<");
            }

            logger.info(">>> Autowired processor stop. <<<");
        }
    }

    /**
     * @param originalValue bundleKey in the bundle of Intent
     * @param statement     original statement
     * @param type          type of data in the  bundle
     * @param isActivity    true as Activity, false as Fragment/Fragment_V4
     * @return statement
     */
    private String buildStatement(String originalValue, String statement, int type, boolean isActivity) {

        //Activity.getIntent().getXXExtra(); Fragment.getIntent().getXX();

        if (type == Type.BOOLEAN.ordinal()) {
            statement += (isActivity ? ("getBooleanExtra($S, " + originalValue + ")") : ("getBoolean($S)"));
        } else if (type == Type.BYTE.ordinal()) {
            statement += (isActivity ? ("getByteExtra($S, " + originalValue + ")") : ("getByte($S)"));
        } else if (type == Type.SHORT.ordinal()) {
            statement += (isActivity ? ("getShortExtra($S, " + originalValue + ")") : ("getShort($S)"));
        } else if (type == Type.INT.ordinal()) {
            statement += (isActivity ? ("getIntExtra($S, " + originalValue + ")") : ("getInt($S)"));
        } else if (type == Type.LONG.ordinal()) {
            statement += (isActivity ? ("getLongExtra($S, " + originalValue + ")") : ("getLong($S)"));
        } else if (type == Type.CHAR.ordinal()) {
            statement += (isActivity ? ("getCharExtra($S, " + originalValue + ")") : ("getChar($S)"));
        } else if (type == Type.FLOAT.ordinal()) {
            statement += (isActivity ? ("getFloatExtra($S, " + originalValue + ")") : ("getFloat($S)"));
        } else if (type == Type.DOUBLE.ordinal()) {
            statement += (isActivity ? ("getDoubleExtra($S, " + originalValue + ")") : ("getDouble($S)"));
        } else if (type == Type.STRING.ordinal()) {
            statement += (isActivity ? ("getStringExtra($S)") : ("getString($S)"));
        } else if (type == Type.PARCELABLE.ordinal()) {
            statement += (isActivity ? ("getParcelableExtra($S)") : ("getParcelable($S)"));
        } else if (type == Type.OBJECT.ordinal()) {
            statement = "jsonService.parseObject(substitute." +
                    (isActivity ? "getIntent()." : "getArguments().") +
                    (isActivity ? "getStringExtra($S)" : "getString($S)") + ", $T.class)";
        }

        return statement;
    }

    /**
     * Categories field, find his papa.
     *
     * @param elements Field need autowired
     */
    private void categories(Set<? extends Element> elements) throws IllegalAccessException {
        if (CollectionUtils.isNotEmpty(elements)) {
            for (Element element : elements) {
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

                if (element.getModifiers().contains(Modifier.PRIVATE)) {
                    throw new IllegalAccessException("The autowired fields CAN NOT BE 'private'!!! please check field ["
                            + element.getSimpleName() + "] in class [" + enclosingElement.getQualifiedName() + "]");
                }

                if (parentAndChild.containsKey(enclosingElement)) { // Has categries
                    parentAndChild.get(enclosingElement).add(element);
                } else {
                    List<Element> childs = new ArrayList<>();
                    childs.add(element);
                    parentAndChild.put(enclosingElement, childs);
                }
            }

            logger.info("categories finished.");
        }
    }

}
