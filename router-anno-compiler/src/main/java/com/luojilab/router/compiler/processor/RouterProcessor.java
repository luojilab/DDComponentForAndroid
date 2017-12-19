package com.luojilab.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.luojilab.router.compiler.utils.AnnoUtils;
import com.luojilab.router.compiler.utils.Logger;
import com.luojilab.router.compiler.utils.TypeUtils;
import com.luojilab.router.facade.annotation.Autowired;
import com.luojilab.router.facade.annotation.RouteNode;
import com.luojilab.router.facade.enums.NodeType;
import com.luojilab.router.facade.model.Node;
import com.luojilab.router.facade.utils.RouteUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import static com.luojilab.router.compiler.utils.Constants.ACTIVITY;
import static com.luojilab.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTER;
import static com.luojilab.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTE_NODE;
import static com.luojilab.router.compiler.utils.Constants.ICOMPONENTROUTER;
import static com.luojilab.router.compiler.utils.Constants.KEY_HOST_NAME;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * <p><b>Package:</b> com.luojilab.router.compiler.processor </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> RouterProcessor </p>
 * <p><b>Description:</b> generate RouterLoader class for 'Router' annotated class,
 * parse 'RouteNode' annotated Activities to mapper
 * </p>
 * Created by leobert on 2017/9/18.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_HOST_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ANNOTATION_TYPE_ROUTE_NODE, ANNOTATION_TYPE_ROUTER})
public class RouterProcessor extends AbstractProcessor {
    private Logger logger;

    private Filer mFiler;
    private Types types;
    private Elements elements;

    private ArrayList<Node> routerNodes;

    private TypeMirror type_TextUtils;
    //    private TypeMirror type_List;
    private TypeMirror type_String;
    private TypeMirror type_Context;
    private TypeMirror type_Bundle;
    private TypeMirror type_Integer;
    private TypeMirror type_Uri;
    private TypeUtils typeUtils;

    private TypeName tn_ListString;

    private String host = null;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        routerNodes = new ArrayList<>();

        mFiler = processingEnv.getFiler();   // Generate class.
        types = processingEnv.getTypeUtils();            // Get type utils.
        elements = processingEnv.getElementUtils();      // Get class meta.
        typeUtils = new TypeUtils(types, elements);

        logger = new Logger(processingEnv.getMessager());   // Package the log utils.

        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            host = options.get(KEY_HOST_NAME);
            logger.info(">>> host is " + host + " <<<");
        }

        type_TextUtils = elements.getTypeElement("android.text.TextUtils").asType();
        type_String = elements.getTypeElement("java.lang.String").asType();
        type_Context = elements.getTypeElement("android.content.Context").asType();
        type_Bundle = elements.getTypeElement("android.os.Bundle").asType();
        type_Uri = elements.getTypeElement("android.net.Uri").asType();
        type_Integer = elements.getTypeElement("java.lang.Integer").asType();

        ClassName string = ClassName.get("java.lang", "String");
        ClassName list = ClassName.get("java.util", "List");
        tn_ListString = ParameterizedTypeName.get(list, string);

        logger.info(">>> RouteProcessor init. <<<");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> routeNodes = roundEnvironment.getElementsAnnotatedWith(RouteNode.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                parseRouteNodes(routeNodes);
            } catch (Exception e) {
                logger.error(e);
            }
            generateRouterImpl();
            return true;
        }
        return false;
    }


    private void generateRouterImpl() {

        String claName = RouteUtils.genHostUIRouterClass(host);

        //pkg
        String pkg = claName.substring(0, claName.lastIndexOf("."));
        //simpleName
        String cn = claName.substring(claName.lastIndexOf(".") + 1);
        // superInterface ClassName
        ClassName superInterface = ClassName.get(elements.getTypeElement(ICOMPONENTROUTER));

        logger.info(">>> :tag:");

        // private static Map<String,Class> routeMapper = new HashMap<String.Class>();
        FieldSpec routeMapperField = generateRouteMapperFieldSpec();

        FieldSpec paramsMapperField = generateParamsMapperFieldSpec();

        //private static final String HOST = "xxx"
        FieldSpec hostField = generateHostFieldSpec(host);
        CodeBlock initRouterBlock = generateInitCodeBlock();
        CodeBlock initParamsBlock = generateInitParamsCodeBlock();
        MethodSpec openUrl = generateOpenUri1();
        MethodSpec openUri = generateOpenUri2();
        MethodSpec openUrlForResult = generateOpenUri3();
        MethodSpec openUriForResult = generateOpenUri4();
        MethodSpec verify = generateVerify();

        try {
            JavaFile.builder(pkg, TypeSpec.classBuilder(cn)
                    .addModifiers(PUBLIC)
                    .addSuperinterface(superInterface)
                    .addField(routeMapperField)
                    .addField(paramsMapperField)
                    .addField(hostField)
                    .addStaticBlock(initRouterBlock)
                    .addStaticBlock(initParamsBlock)
                    .addMethod(openUrl)
                    .addMethod(openUri)
                    .addMethod(openUrlForResult)
                    .addMethod(openUriForResult)
                    .addMethod(verify)
                    .build()
            ).build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRouteNodes(Set<? extends Element> routeElements) {

        TypeMirror type_Activity = elements.getTypeElement(ACTIVITY).asType();

        for (Element element : routeElements) {
            TypeMirror tm = element.asType();
            RouteNode route = element.getAnnotation(RouteNode.class);

            if (types.isSubtype(tm, type_Activity)) {                 // Activity
                logger.info(">>> Found activity route: " + tm.toString() + " <<<");

                Node node = new Node();
                String path = route.path();

                checkPath(path);

                node.setPath(path);
                node.setPriority(route.priority());
                node.setNodeType(NodeType.ACTIVITY);
                node.setRawType(element);

                Map<String, Integer> paramsType = new HashMap<>();
                for (Element field : element.getEnclosedElements()) {
                    if (field.getKind().isField() && field.getAnnotation(Autowired.class) != null) {
                        Autowired paramConfig = field.getAnnotation(Autowired.class);
                        paramsType.put(StringUtils.isEmpty(paramConfig.name())
                                ? field.getSimpleName().toString() : paramConfig.name(), typeUtils.typeExchange(field));
                    }
                }
                node.setParamsType(paramsType);

                if (!routerNodes.contains(node)) {
                    routerNodes.add(node);
                }
            } else {
                throw new IllegalStateException("only activity can be annotated by RouteNode");
            }
        }
    }

    private void checkPath(String path) {
        if (path == null || path.isEmpty() || !path.startsWith("/"))
            throw new IllegalArgumentException("path cannot be null or empty,and should start with /,this is:" + path);

        if (path.contains("//") || path.contains("&") || path.contains("?"))
            throw new IllegalArgumentException("path should not contain // ,& or ?,this is:" + path);

        if (path.endsWith("/"))
            throw new IllegalArgumentException("path should not endWith /,this is:" + path
                    + ";or append a token:index");
    }

    private static final String mRouteMapperFieldName = "routeMapper";
    private static final String mParamsMapperFieldName = "paramsMapper";

    private FieldSpec generateRouteMapperFieldSpec() {
        ParameterizedTypeName routeMapperFieldTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Class.class)
        );

        ParameterizedTypeName routeMapperFieldInitializeTypeName =
                ParameterizedTypeName.get(HashMap.class, String.class,
                        Class.class);

        return FieldSpec
                .builder(routeMapperFieldTypeName, mRouteMapperFieldName, Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T()", routeMapperFieldInitializeTypeName)
                .build();
    }

    private FieldSpec generateParamsMapperFieldSpec() {

        ParameterizedTypeName bean = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Integer.class)
        );

        ParameterizedTypeName routeMapperFieldTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(Class.class),
                bean
        );

        return FieldSpec
                .builder(routeMapperFieldTypeName, mParamsMapperFieldName, Modifier.PRIVATE, Modifier.STATIC)
                .build();
    }

    private FieldSpec generateHostFieldSpec(String host) {
        return FieldSpec
                .builder(String.class, "HOST", Modifier.PRIVATE, Modifier.STATIC, FINAL)
                .initializer("$S", host)
                .build();
    }

    private CodeBlock generateInitCodeBlock() {
        CodeBlock.Builder initBlockBuilder = CodeBlock.builder();

        for (Node node : routerNodes) {
            initBlockBuilder.addStatement(
                    mRouteMapperFieldName + ".put($S,$T.class)",
                    node.getPath(),
                    ClassName.get((TypeElement) node.getRawType()));
        }

        return initBlockBuilder.build();
    }

    private CodeBlock generateInitParamsCodeBlock() {
        CodeBlock.Builder initBlockBuilder = CodeBlock.builder();
        initBlockBuilder.addStatement(mParamsMapperFieldName + "= new HashMap<>()");

        for (Node node : routerNodes) {
            // Make map body for paramsType
            StringBuilder mapBodyBuilder = new StringBuilder();
            Map<String, Integer> paramsType = node.getParamsType();
            if (MapUtils.isNotEmpty(paramsType)) {
                for (Map.Entry<String, Integer> types : paramsType.entrySet()) {
                    mapBodyBuilder.append("put(\"").append(types.getKey()).append("\", ").append(types.getValue()).append("); ");
                }
            }
            String mapBody = mapBodyBuilder.toString();

            logger.info(">>> mapBody: " + mapBody + " <<<");

            if (!StringUtils.isEmpty(mapBody)) {
                initBlockBuilder.addStatement(
                        mParamsMapperFieldName + ".put($T.class,"
                                + "new java.util.HashMap<String, Integer>(){{" + mapBody + "}}" + ")",
                        ClassName.get((TypeElement) node.getRawType()));
            }
        }
        return initBlockBuilder.build();
    }

    /**
     * public boolean openUri(Context context, String url, Bundle bundle) {
     * if (TextUtils.isEmpty(url) || context == null) {
     * return true;
     * }
     * return openUri(context, Uri.parse(url), bundle, 0);
     * }
     *
     * @return
     */
    private MethodSpec generateOpenUri1() {
        TypeName returnType = TypeName.BOOLEAN;
        String returnStatement = "return openUri(context, Uri.parse(url), bundle,0)";

        ParameterSpec contextSpec =
                AnnoUtils.generateMethodParameterSpec(type_Context, "context");

        ParameterSpec urlSpec =
                AnnoUtils.generateMethodParameterSpec(type_String, "url");

        ParameterSpec bundleSpec =
                AnnoUtils.generateMethodParameterSpec(type_Bundle, "bundle");

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("openUri")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addParameter(contextSpec)
                .addParameter(urlSpec)
                .addParameter(bundleSpec);

        openUriMethodSpecBuilder.beginControlFlow("if ($T.isEmpty(url) || context == null)",
                type_TextUtils);
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        openUriMethodSpecBuilder.addStatement(returnStatement);

        return openUriMethodSpecBuilder.build();
    }

    /**
     * public boolean openUri(Context context, Uri uri, Bundle bundle) {
     * return openUri(context, uri, bundle, 0);
     * }
     */
    private MethodSpec generateOpenUri2() {
        TypeName returnType = TypeName.BOOLEAN;
        String returnStatement = "return openUri(context, uri, bundle,0)";

        ParameterSpec contextSpec =
                AnnoUtils.generateMethodParameterSpec(type_Context, "context");

        ParameterSpec uriSpec =
                AnnoUtils.generateMethodParameterSpec(type_Uri, "uri");

        ParameterSpec bundleSpec =
                AnnoUtils.generateMethodParameterSpec(type_Bundle, "bundle");

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("openUri")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addParameter(contextSpec)
                .addParameter(uriSpec)
                .addParameter(bundleSpec);

        openUriMethodSpecBuilder.addStatement(returnStatement);

        return openUriMethodSpecBuilder.build();

    }


    /**
     * @Override public boolean openUri(Context context, String url, Bundle bundle, int requestCode) {
     * if (TextUtils.isEmpty(url) || context == null) {
     * return true;
     * }
     * return openUri(context, Uri.parse(url), bundle, requestCode);
     * }
     */
    private MethodSpec generateOpenUri3() {
        TypeName returnType = TypeName.BOOLEAN;
        String returnStatement = "return openUri(context, Uri.parse(url), bundle,requestCode)";

        ParameterSpec contextSpec =
                AnnoUtils.generateMethodParameterSpec(type_Context, "context");

        ParameterSpec urlSpec =
                AnnoUtils.generateMethodParameterSpec(type_String, "url");

        ParameterSpec bundleSpec =
                AnnoUtils.generateMethodParameterSpec(type_Bundle, "bundle");

        ParameterSpec intSpec =
                AnnoUtils.generateMethodParameterSpec(type_Integer, "requestCode");

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("openUri")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addParameter(contextSpec)
                .addParameter(urlSpec)
                .addParameter(bundleSpec)
                .addParameter(intSpec);

        openUriMethodSpecBuilder.beginControlFlow("if ($T.isEmpty(url) || context == null)",
                type_TextUtils);
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        openUriMethodSpecBuilder.addStatement(returnStatement);

        return openUriMethodSpecBuilder.build();
    }

    /**
     * public boolean openUri(Context context, Uri uri, Bundle bundle, int requestCode) {
     * if (uri == null || context == null) {
     * return true;
     * }
     * String scheme = uri.getScheme();
     * String host = uri.getHost();
     * if (!HOST.equals(host)) {
     * return false;
     * }
     * List<String> pathSegments = uri.getPathSegments();
     * String path = "/" + TextUtils.join("/", pathSegments);
     * if (routeMapper.containsKey(path)) {
     * Class target = routeMapper.get(path);
     * if (bundle == null) {
     * bundle = new Bundle();
     * }
     * Map<String, String> params = com.luojilab.component.componentlib.utils.UriUtils.parseParams(uri);
     * Map<String, Integer> paramsType = paramsMapper.get(target);
     * com.luojilab.component.componentlib.utils.UriUtils.setBundleValue(bundle, params, paramsType);
     * Intent intent = new Intent(context, target);
     * intent.putExtras(bundle);
     * if (requestCode > 0 && context instanceof Activity) {
     * ((Activity) context).startActivityForResult(intent, requestCode);
     * } else {
     * context.startActivity(intent);
     * }
     * <p>
     * return true;
     * }
     * return false;
     * }
     */
    private MethodSpec generateOpenUri4() {
        TypeName returnType = TypeName.BOOLEAN;

        ParameterSpec contextSpec =
                AnnoUtils.generateMethodParameterSpec(type_Context, "context");

        ParameterSpec uriSpec =
                AnnoUtils.generateMethodParameterSpec(type_Uri, "uri");

        ParameterSpec bundleSpec =
                AnnoUtils.generateMethodParameterSpec(type_Bundle, "bundle");

        ParameterSpec intSpec =
                AnnoUtils.generateMethodParameterSpec(type_Integer, "requestCode");

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("openUri")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addParameter(contextSpec)
                .addParameter(uriSpec)
                .addParameter(bundleSpec)
                .addParameter(intSpec);

        //1
        openUriMethodSpecBuilder.beginControlFlow("if (uri == null || context == null)");
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        //2
        openUriMethodSpecBuilder.addStatement("$T scheme = uri.getScheme()", type_String);
        openUriMethodSpecBuilder.addStatement("$T host = uri.getHost()", type_String);

        //3
        openUriMethodSpecBuilder.beginControlFlow("if (!HOST.equals(host))");
        openUriMethodSpecBuilder.addStatement("return false");
        openUriMethodSpecBuilder.endControlFlow();

        //4
        openUriMethodSpecBuilder.addStatement("$T pathSegments = uri.getPathSegments()", tn_ListString);
        openUriMethodSpecBuilder.addStatement("$T path = \"/\" + $T.join(\"/\",pathSegments)", type_String, type_TextUtils);

        TypeMirror type_Intent =
                elements.getTypeElement("android.content.Intent").asType();

        //5
        openUriMethodSpecBuilder.beginControlFlow("if (routeMapper.containsKey(path))");
        openUriMethodSpecBuilder.addStatement("Class target = routeMapper.get(path)");

        openUriMethodSpecBuilder.beginControlFlow("if (bundle == null)");
        openUriMethodSpecBuilder.addStatement("bundle = new Bundle() ");
        openUriMethodSpecBuilder.endControlFlow();

        openUriMethodSpecBuilder.addStatement("Map<String, String> params = com.luojilab.component.componentlib.utils.UriUtils.parseParams(uri)");
        openUriMethodSpecBuilder.addStatement("Map<String, Integer> paramsType = paramsMapper.get(target)");
        openUriMethodSpecBuilder.addStatement("com.luojilab.component.componentlib.utils.UriUtils.setBundleValue(bundle, params, paramsType)");

        openUriMethodSpecBuilder.addStatement("$T intent = new $T(context, target)", type_Intent, type_Intent);
        openUriMethodSpecBuilder.addStatement("intent.putExtras(bundle)");

        openUriMethodSpecBuilder.beginControlFlow(" if (requestCode > 0 && context instanceof android.app.Activity)");
        openUriMethodSpecBuilder.addStatement("((android.app.Activity) context).startActivityForResult(intent, requestCode)");
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        openUriMethodSpecBuilder.addStatement("context.startActivity(intent)");
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        //6
        openUriMethodSpecBuilder.addStatement("return false");

        return openUriMethodSpecBuilder.build();
    }

    /**
     */
    private MethodSpec generateVerify() {
        TypeName returnType = TypeName.BOOLEAN;

        ParameterSpec uriSpec =
                AnnoUtils.generateMethodParameterSpec(type_Uri, "uri");


        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("verifyUri")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addParameter(uriSpec);

        //4
        openUriMethodSpecBuilder.addStatement("$T host = uri.getHost()", type_String);
        openUriMethodSpecBuilder.addStatement("$T pathSegments = uri.getPathSegments()", tn_ListString);
        openUriMethodSpecBuilder.addStatement("$T path = \"/\" + $T.join(\"/\",pathSegments)", type_String, type_TextUtils);
        openUriMethodSpecBuilder.addStatement("return HOST.equals(host) && routeMapper.containsKey(path)");

        return openUriMethodSpecBuilder.build();
    }

}
