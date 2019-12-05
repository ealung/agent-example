package com;

import java.io.IOException;
import java.lang.instrument.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class AgentDemo {
    //java agent 入口
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        simpleDemo(agentOps, inst);
    }
    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("=========agentmain方法执行========");
        simpleDemo(agentOps, inst);
        //transform是会对尚未加载的类进行增加代理层，这里是已经运行中的jvm，所以类以及被加载了
        //必须主动调用retransformClasses让jvm再对运行中的类进行加上代理层
        for (Class allLoadedClass : inst.getAllLoadedClasses()) {
            //这里的Test路径，修改成你自己机器agent-demo-web工程的Test类的路径
            if(allLoadedClass.getName().contains("com.example.agentdemoweb.Test")){
                try {
                    inst.retransformClasses(allLoadedClass);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void simpleDemo(String agentOps, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //判断是指定的class
                if ("com/example/agentdemoweb/Test".equals(className)) {
                    try {
                        //获取更改后的类class 字节数组
                        String path="/Users/zhangchanglu/IdeaProjects/agent-example/agent-demo-web/src/main/resources/Test.class";
                        classfileBuffer = Files.readAllBytes(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return classfileBuffer;
            }
        },true);
    }


//
//    /**
//     * 打印已经加载的class
//     * @param agentOps
//     * @param inst
//     */
//    public static void soutClass(String agentOps, Instrumentation inst){
//        inst.addTransformer(new ClassFileTransformer() {
//            @Override
//            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//                System.out.println(className);
//                return classfileBuffer;
//            }
//        });
//    }
//    /**
//     * 使用bytebuddy来实现类的增强
//     * @param agentOps
//     * @param inst
//     */
//    public static void buttyBuddyDemo(String agentOps, Instrumentation inst) {
//        new AgentBuilder.Default()
//                .type(any())
//                .transform((builder, typeDescription, classLoader, javaModule) ->
//                        builder
//                                .method(ElementMatchers.named("test"))//test 方法
//                                .intercept(MethodDelegation.to(TimeInterceptor.class))
//                )
//                .installOn(inst);
//    }
//
//    /**
//     * 对Test类进行修改
//     * @param agentOps
//     * @param inst¬
//     */
//    public static void enhanceTest(String agentOps, Instrumentation inst) {
//        inst.addTransformer(new ClassFileTransformer() {
//            @Override
//            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//                //判断是指定的class
//                if ("com/example/agentdemoweb/Test".equals(className)) {
//                    try {
//                        //获取更改后的类class 字节数组
//                        classfileBuffer = Files.readAllBytes(Paths.get("/Users/zhangchanglu/IdeaProjects/agent-demo/src/main/resouces/Test.class"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return classfileBuffer;
//            }
//        });
//    }
}
