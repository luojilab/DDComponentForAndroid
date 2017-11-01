package com.ljsw.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.ljsw.router.compiler.model.GroupInfo;
import com.ljsw.router.compiler.utils.AnnoUtils;
import com.ljsw.router.compiler.utils.Logger;
import com.ljsw.router.compiler.utils.TypeUtils;
import com.ljsw.router.facade.Constants;
import com.ljsw.router.facade.annotation.Autowired;
import com.ljsw.router.facade.annotation.RouteNode;
import com.ljsw.router.facade.annotation.Router;
import com.ljsw.router.facade.annotation.UiRoutersHolder;
import com.ljsw.router.facade.enums.NodeType;
import com.ljsw.router.facade.model.Address;
import com.ljsw.router.facade.model.Node;
import com.ljsw.router.facade.model.NodeParamsConfig;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

import static com.ljsw.router.compiler.utils.Constants.ACTIVITY;
import static com.ljsw.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTER;
import static com.ljsw.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTE_NODE;
import static com.ljsw.router.compiler.utils.Constants.ANNOTATION_TYPE_UIROUTERSHOLDER;
import static com.ljsw.router.compiler.utils.Constants.KEY_MODULE_NAME;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * <p><b>Package:</b> com.ljsw.router.compiler.processor </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> RouterProcessor </p>
 * <p><b>Description:</b> generate RouterLoader class for 'Router' annotated class,
 * parse 'RouteNode' annotated Activities to mapper
 * </p>
 * Created by leobert on 2017/9/18.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//@SupportedAnnotationTypes({
//        ANNOTATION_TYPE_ROUTE_NODE,
//        ANNOTATION_TYPE_ROUTER,
//        ANNOTATION_TYPE_UIROUTERSHOLDER})
public class RouterProcessor extends AbstractProcessor {
    private Logger logger;

    private Filer mFiler;       // File util, write class file into disk.
    private Types types;
    private Elements elements;

    /**
     * (group,hostInfo)
     */
    private Map<String, GroupInfo> routers;
    /**
     * (group,List(Node))
     */
    private Map<String, List<Node>> routerNodes;

    private TypeMirror type_TextUtils;
    private TypeMirror type_String;
    private TypeMirror type_Context;
    private TypeMirror type_Bundle;
    private TypeMirror type_Uri;

    private TypeName tn_ListString;

    private TypeUtils typeUtils;

    private String PATH_BEAN_PARAMSCONFIG = NodeParamsConfig.class.getName();

    private String PATH_UTIL_PARAMPARSER;//= "osp.leobert.android.component.router.utils.ParamsUtils";

    private String PATH_BEAN_ADDRESS = Address.class.getName();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(
                ANNOTATION_TYPE_ROUTE_NODE,
                ANNOTATION_TYPE_ROUTER,
                ANNOTATION_TYPE_UIROUTERSHOLDER
        ));
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        logger = new Logger(processingEnv.getMessager());   // Package the log utils.
        logger.info(">>>>parse options<<<");
        parseOptionParams(processingEnv.getOptions());

        routers = new HashMap<>();
        routerNodes = new HashMap<>();

        mFiler = processingEnv.getFiler();   // Generate class.
        types = processingEnv.getTypeUtils();            // Get type utils.
        elements = processingEnv.getElementUtils();      // Get class meta.

        logger.info("init");


        type_TextUtils = elements.getTypeElement("android.text.TextUtils").asType();
        type_String = elements.getTypeElement("java.lang.String").asType();
        type_Context = elements.getTypeElement("android.content.Context").asType();
        type_Bundle = elements.getTypeElement("android.os.Bundle").asType();
        type_Uri = elements.getTypeElement("android.net.Uri").asType();

        ClassName string = ClassName.get("java.lang", "String");
        ClassName list = ClassName.get("java.util", "List");
        tn_ListString = ParameterizedTypeName.get(list, string);

        typeUtils = new TypeUtils(types, elements);

        logger.info(">>> RouteProcessor init. <<<");
    }

    private void parseOptionParams(Map<String, String> options) {
        if (MapUtils.isNotEmpty(options)) {
            PATH_UTIL_PARAMPARSER = options.get("path_util_paramparser");
        }


        if (StringUtils.isEmpty(PATH_UTIL_PARAMPARSER)) {
            logger.error("please set 'path_util_paramparser' in options");
            throw new IllegalStateException("no path_util_paramparser found");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> routeService =
                    roundEnvironment.getElementsAnnotatedWith(Router.class);
            try {
                logger.info(">>> Found routeService <<<");
                this.foundRouters(routeService);
            } catch (Exception e) {
                logger.error(e);
            }


            Set<? extends Element> routeNodes =
                    roundEnvironment.getElementsAnnotatedWith(RouteNode.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                this.parseRouteNodes(routeNodes);
            } catch (Exception e) {
                logger.error(e);
            }

            this.generateRouterImpl();

            Set<? extends Element> placeHolder =
                    roundEnvironment.getElementsAnnotatedWith(UiRoutersHolder.class);


            try {
                logger.info(">>> Generate PlaceHolder, start... <<<");
                this.generatePlaceHolder(placeHolder);
            } catch (Exception e) {
                logger.error(e);
            }

            return true;
        }

        return false;
    }


    private void generateRouterImpl() {
        Set<String> groups = routers.keySet();

        for (String group : groups) {
            logger.info(">>> write for group:" + group);
            GroupInfo groupInfo = routers.get(group);

            String claName = groupInfo.getOutPutPath();
            //pkg
            String pkg = claName.substring(0, claName.lastIndexOf("."));
            //simpleName
            String cn = claName.substring(claName.lastIndexOf(".") + 1);
            // superInterface ClassName
            ClassName superInterface = ClassName.get(elements.getTypeElement(groupInfo.getInterfacePath()));
            // private static Map<String,Class> routeMapper = new HashMap<String,Class>();
            FieldSpec routeMapperField = generateRouteMapperFieldSpec();
            //private static final String HOST = "xxx"
            FieldSpec hostField = generateHostFieldSpec(groupInfo.getHost());
            //private static Map<String, Integer> paramsType = new HashMap<String,Integer>();
            FieldSpec paramsConfigsTypeField = generateParamsConfigFieldSpec();


            /*
            * static {
            *       routeMapper.put(...,...);
            *       //...
            * }
            * */
            CodeBlock initBlock = generateInitCodeBlock(group);


            MethodSpec openUrl = generateOpenUri1();

            MethodSpec openUri = generateOpenUri2();
            MethodSpec verify = generateVerify();


            logger.info(">>>onWrite :group:" + group);
            //generate class file
            try {
                JavaFile.builder(pkg, TypeSpec.classBuilder(cn)
                        .addModifiers(PUBLIC)
                        .addSuperinterface(superInterface)
                        .addField(routeMapperField)
                        .addField(hostField)
                        .addField(paramsConfigsTypeField)
                        .addStaticBlock(initBlock)
                        .addMethod(openUrl)
                        .addMethod(openUri)
                        .addMethod(verify)
                        .build()
                ).build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void parseRouteNodes(Set<? extends Element> routeElements) {

        TypeMirror type_Activity = elements.getTypeElement(ACTIVITY).asType();

        Map<String, List<String>> cacheForConflictCheck = new HashMap<>();

        for (Element element : routeElements) {
            TypeMirror tm = element.asType();
            RouteNode route = element.getAnnotation(RouteNode.class);

            if (types.isSubtype(tm, type_Activity)) {                 // Activity
                logger.info(">>> Found activity route: " + tm.toString() + " <<<");

                String group = route.group();
                List<String> groupPaths;

                if (cacheForConflictCheck.containsKey(group)) {
                    groupPaths = cacheForConflictCheck.get(group);
                } else {
                    groupPaths = new ArrayList<>();
                    cacheForConflictCheck.put(group, groupPaths);
                }

                // get fields need to autowired
                Map<String, Integer> paramsType = new HashMap<>();
                for (Element field : element.getEnclosedElements()) {

                    // TODO: 2017/9/29 consider if Autowired support componentService found one day

                    if (field.getKind().isField() && field.getAnnotation(Autowired.class) != null) {
                        Autowired paramConfig = field.getAnnotation(Autowired.class);
                        paramsType.put(StringUtils.isEmpty(paramConfig.name()) ?
                                        field.getSimpleName().toString() : paramConfig.name(),
                                typeUtils.typeExchange(field));
                    }
                }

                Node node = new Node();
                String path = route.path();

                checkPath(group, path, groupPaths);

                groupPaths.add(path);

                node.setPath(path);
                node.setPriority(route.priority());
                node.setNodeType(NodeType.ACTIVITY);
                node.setRawType(element);
                node.setParamsType(paramsType);

                if (routerNodes.containsKey(group)) {
                    routerNodes.get(group).add(node);
                } else {
                    ArrayList<Node> nodes = new ArrayList<>();
                    nodes.add(node);
                    routerNodes.put(group, nodes);
                }
            } else {
                throw new IllegalStateException("only activity can be annotated by RouteNode");
            }
        }
    }

    private void checkPath(String group, String path, List<String> groupPaths) {
        if (groupPaths.contains(path))
            throw new IllegalStateException("conflict path in group:" + group + ",path is:" + path);

        if (path == null || path.isEmpty() || !path.startsWith("/"))
            throw new IllegalArgumentException("path cannot be null or empty,and should start with /,this is:" + path);

        if (path.contains("//") || path.contains("&") || path.contains("?"))
            throw new IllegalArgumentException("path should not contain // ,& or ?,this is:" + path);

        if (path.endsWith("/"))
            throw new IllegalArgumentException("path should not endWith /,this is:" + path
                    + ";or append a token:index");
    }

    private void foundRouters(Set<? extends Element> routers) {
        for (Element element : routers) {
            Router router = element.getAnnotation(Router.class);
            String group = router.group();
            String host = router.host();
            if (this.routers.containsKey(group))
                throw new IllegalStateException("duplicated group at annotation," +
                        "please check group:" + group);

            String outPutPath = Constants.ROUTERIMPL_OUTPUT_PKG +
                    Constants.DOT + group + Constants.DOT + element.getSimpleName() + "Impl";

            String interfacePath = ((TypeElement) element).getQualifiedName().toString();
            this.routers.put(group, new GroupInfo(host, group, router.alias(), outPutPath, interfacePath));
        }
    }

    private static final String mRouteMapperFieldName = "routeMapper";
    private static final String mParamsConfigFieldName = "paramsConfigs";

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

    private FieldSpec generateHostFieldSpec(String host) {
        return FieldSpec
                .builder(String.class, "HOST", Modifier.PRIVATE, Modifier.STATIC, FINAL)
                .initializer("$S", host)
                .build();
    }

    private FieldSpec generateParamsConfigFieldSpec() {
        ClassName paramsConfigCn =
                ClassName.get(NodeParamsConfig.class.getPackage().getName(),
                        NodeParamsConfig.class.getSimpleName());

        ClassName hashMapCn = ClassName.get("java.util", "HashMap");

        TypeMirror type_ParamsConfig =
                elements.getTypeElement(PATH_BEAN_PARAMSCONFIG).asType();

        TypeName tn =
                ParameterizedTypeName.get(type_ParamsConfig);

        ParameterizedTypeName paramsConfigFieldTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                paramsConfigCn);


        ParameterizedTypeName paramsConfigFieldInitializeTypeName =
                ParameterizedTypeName.get(hashMapCn,
                        ParameterizedTypeName.get(type_String),
                        tn);

        return FieldSpec
                .builder(paramsConfigFieldTypeName, mParamsConfigFieldName, Modifier.PRIVATE, Modifier.STATIC)
                .initializer("new $T()", paramsConfigFieldInitializeTypeName)
                .build();
    }

    private CodeBlock generateInitCodeBlock(String group) {
        CodeBlock.Builder initBlockBuilder = CodeBlock.builder();

        List<Node> nodes = routerNodes.get(group);
        if (nodes == null) {
            logger.error("No RouterNode defined for group:" + group);
            return initBlockBuilder.build();
        }

        ClassName paramsConfigCn =
                ClassName.get(NodeParamsConfig.class.getPackage().getName(),
                        NodeParamsConfig.class.getSimpleName());

        int i = 0;
        for (Node node : nodes) {
            initBlockBuilder.addStatement(
                    mRouteMapperFieldName + ".put($S,$T.class)",
                    node.getPath(),
                    ClassName.get((TypeElement) node.getRawType()));

            String configName = "config" + i;

            Map<String, Integer> paramsType = node.getParamsType();
            if (paramsType == null || paramsType.isEmpty())
                continue;

            initBlockBuilder.addStatement("$T $L = new $T()",
                    paramsConfigCn, configName, paramsConfigCn);

            for (String name : paramsType.keySet()) {
                initBlockBuilder.addStatement(
                        configName + ".add($S,$L)",
                        name, paramsType.get(name));
            }

            initBlockBuilder.addStatement(
                    mParamsConfigFieldName + ".put($S,$L)",
                    node.getPath(), configName);
            i++;
        }

        return initBlockBuilder.build();
    }

    /**
     * public boolean openUri(Context context, String url, Bundle bundle) {
     * if (TextUtils.isEmpty(url) || context == null) {
     * return true;
     * }
     * return openUri(context, Uri.parse(url), bundle);
     * }
     *
     * @return
     */
    private MethodSpec generateOpenUri1() {
        TypeName returnType = TypeName.BOOLEAN;
        String returnStatement = "return openUri(context, Uri.parse(url), bundle)";

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
     * //@Override
     * public boolean openUri(Context context, Uri uri, Bundle bundle) {
     * 1
     * if (uri == null || context == null) {
     * return true;
     * }
     * <p>
     * 2
     * //String scheme = uri.getScheme();
     * String host = uri.getHost();
     * <p>
     * 3
     * if (!HOST.equals(host)) {
     * return false;
     * }
     * <p>
     * 4
     * List<String> pathSegments = uri.getPathSegments();
     * String path = AnnoUtils.join(pathSegments,"/");
     * <p>
     * 5
     * if (routeMapper.containsKey(path)) {
     * Class target = routeMapper.get(path);
     * Intent intent = new Intent(context, target);
     * intent.setData(uri)
     * intent.putExtras(bundle == null ? new Bundle() : bundle);
     * //暂未支持startActivityForResult
     * context.startActivity(intent);
     * return true;
     * }
     * <p>
     * return false;
     * }
     */
    private MethodSpec generateOpenUri2() {
        TypeName returnType = TypeName.BOOLEAN;

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

        //1
        openUriMethodSpecBuilder.beginControlFlow("if (uri == null || context == null)");
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        //2
        //openUriMethodSpecBuilder.addStatement("$T scheme = uri.getScheme()", type_String);
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

        TypeMirror type_Util = elements.getTypeElement(PATH_UTIL_PARAMPARSER).asType();

        //5
        openUriMethodSpecBuilder.beginControlFlow("if (routeMapper.containsKey(path))");
        openUriMethodSpecBuilder.addStatement("Class target = routeMapper.get(path)");
        openUriMethodSpecBuilder.addStatement("$T intent = new $T(context, target)", type_Intent, type_Intent);
        openUriMethodSpecBuilder.addStatement("intent.setData(uri)");

        openUriMethodSpecBuilder.addStatement("bundle = $T.parseIfNeed(bundle,uri,$L.get(path))",
                type_Util, mParamsConfigFieldName);

        openUriMethodSpecBuilder.addStatement("intent.putExtras(bundle == null ? new Bundle() : bundle)");
        openUriMethodSpecBuilder.addStatement("context.startActivity(intent)");
        openUriMethodSpecBuilder.addStatement("return true");
        openUriMethodSpecBuilder.endControlFlow();

        //6
        openUriMethodSpecBuilder.addStatement("return false");

        return openUriMethodSpecBuilder.build();
    }

    /**
     * //@Override
     * public boolean verifyUri(Uri uri) {
     * String host = uri.getHost();
     * List<String> pathSegments = uri.getPathSegments();
     * String path = TextUtils.join("/",pathSegments);
     * return HOST.equals(host) && routeMapper.containsKey(path);
     * }
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


    ///////////////////////////////////////////////////////////////////////////
    // generate PlaceHolder of Address to UiActivity
    ///////////////////////////////////////////////////////////////////////////

    private void generatePlaceHolder(Set<? extends Element> placeHolder) throws IOException {

        Set<String> groups = routers.keySet();

        TypeMirror type_Address = elements.getTypeElement(PATH_BEAN_ADDRESS).asType();

        TypeName addressFieldTypeName = TypeName.get(type_Address);

        for (Element element : placeHolder) {
            String path = ((TypeElement) element).getQualifiedName().toString();
            String pkg = path.substring(0, path.lastIndexOf("."));

            String rootClzName = "$$" + element.getSimpleName().toString();
            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(rootClzName)
                    .addJavadoc("It's just a \" chicken ribs \" - tasteless when eaten but a pity to throw away")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            for (String group : groups) {
                GroupInfo info = routers.get(group);

                String innerClzName = info.getAlias();
                ClassName groupClzName = ClassName.get(pkg, rootClzName, innerClzName);

                TypeSpec.Builder groupClzTypeBuilder = TypeSpec.classBuilder(groupClzName.simpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
                List<Node> nodes = routerNodes.get(group);

                for (Node node : nodes) {
                    groupClzTypeBuilder.addField(FieldSpec.builder(
                            addressFieldTypeName,
                            Node.formatAlias(node),
                            Modifier.PUBLIC,
                            Modifier.STATIC,
                            Modifier.FINAL).initializer("new $T($S,$S)", type_Address, info.getHost(), node.getPath())
                            .build()
                    );
                }

                typeSpecBuilder.addType(groupClzTypeBuilder.build());
            }
            JavaFile.builder(pkg, typeSpecBuilder.build()).build().writeTo(mFiler);
        }
    }

}