package com.ljsw.router.compiler.processor;

import com.google.auto.service.AutoService;
import com.ljsw.router.compiler.utils.Logger;
import com.ljsw.router.facade.annotation.RouteNode;
import com.ljsw.router.facade.annotation.Router;
import com.ljsw.router.facade.enums.NodeType;
import com.ljsw.router.facade.model.Node;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.ljsw.router.compiler.utils.Constants.ACTIVITY;
import static com.ljsw.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTER;
import static com.ljsw.router.compiler.utils.Constants.ANNOTATION_TYPE_ROUTE_NODE;
import static com.ljsw.router.compiler.utils.Constants.KEY_MODULE_NAME;
import static com.ljsw.router.compiler.utils.Constants.ROUTER_UTIL_METHOD_ADDTO;
import static com.ljsw.router.compiler.utils.Constants.TYPE_UIROUTER_LOADER;
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
@SupportedAnnotationTypes({ANNOTATION_TYPE_ROUTE_NODE, ANNOTATION_TYPE_ROUTER})
public class RouterProcessor extends AbstractProcessor {
    private Logger logger;

    private Filer mFiler;       // File util, write class file into disk.
    private Types types;
    private Elements elements;

    private Map<String, TypeMirror> routers;
    private Map<String, List<Node>> routerNodes;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        routers = new HashMap<>();
        routerNodes = new HashMap<>();

        mFiler = processingEnv.getFiler();   // Generate class.
        types = processingEnv.getTypeUtils();            // Get type utils.
        elements = processingEnv.getElementUtils();      // Get class meta.

        logger = new Logger(processingEnv.getMessager());   // Package the log utils.

        logger.info(">>> RouteProcessor init. <<<");
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


            Set<? extends Element> routeNodes = roundEnvironment.getElementsAnnotatedWith(RouteNode.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                this.parseRouteNodes(routeNodes);
            } catch (Exception e) {
                logger.error(e);
            }

            this.write2Util();

            return true;
        }

        return false;
    }

    private void write2Util() {
        Set<String> groups = routers.keySet();

        ClassName type_UiRouterLoader = ClassName.get(elements.getTypeElement(TYPE_UIROUTER_LOADER));
        for (String group : groups) {
            logger.info(">>> write for group:" + group);

            TypeMirror value = routers.get(group);
            String path = value.toString();

            String pkg = path.substring(0, path.lastIndexOf("."));
            String cn = path.substring(path.lastIndexOf(".") + 1) + "Loader";

            ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(Class.class)
            );

            ParameterSpec groupParamSpec =
                    ParameterSpec.builder(inputMapTypeOfGroup, "mapper").build();


            MethodSpec.Builder loadIntoMethodOfRootBuilder =
                    MethodSpec.methodBuilder(ROUTER_UTIL_METHOD_ADDTO)
                            .addParameter(groupParamSpec)
                            .addAnnotation(Override.class)
                            .addModifiers(PUBLIC);

            List<Node> nodes = routerNodes.get(group);
            for (Node node : nodes) {
                loadIntoMethodOfRootBuilder.addStatement(
                        "mapper.put($S,$T.class)",
                        node.getPath(),
                        ClassName.get((TypeElement) node.getRawType()));
            }


            try {
                JavaFile.builder(pkg, TypeSpec.classBuilder(cn)
                        .addModifiers(PUBLIC)
                        .addSuperinterface(type_UiRouterLoader)
                        .addMethod(loadIntoMethodOfRootBuilder.build())
                        .build()
                ).build().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseRouteNodes(Set<? extends Element> routeElements) {

        TypeMirror type_Activity = elements.getTypeElement(ACTIVITY).asType();

        for (Element element : routeElements) {
            TypeMirror tm = element.asType();
            RouteNode route = element.getAnnotation(RouteNode.class);

            if (types.isSubtype(tm, type_Activity)) {                 // Activity
                logger.info(">>> Found activity route: " + tm.toString() + " <<<");

                String group = route.group();

                Node node = new Node();
                node.setPath(route.path());
                node.setPriority(route.priority());
                node.setNodeType(NodeType.ACTIVITY);
                node.setRawType(element);

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

    private void foundRouters(Set<? extends Element> routers) {
        for (Element element : routers) {
            Router router = element.getAnnotation(Router.class);
            String group = router.group();
            if (this.routers.containsKey(group))
                throw new IllegalStateException("duplicated group at annotation," +
                        "please check group:" + group);

            //use exception to get path
            TypeMirror value = null;
            try {
                router.classPath();
            } catch (MirroredTypeException mte) {
                value = mte.getTypeMirror();
            }

            this.routers.put(group, value);
        }
    }
}
