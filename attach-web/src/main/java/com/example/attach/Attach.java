package com.example.attach;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author zhangchanglu
 * @since 2019/09/29 10:15.
 */
public class Attach {
    public static void main(String[] args) {
        //查找所有jvm进程，排除attach测试工程
        List<VirtualMachineDescriptor> attach = VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    return !jvm.displayName().contains("Attach");
                }).collect(Collectors.toList());
        for (int i = 0; i < attach.size(); i++) {
            System.out.println("[" + i + "] " + attach.get(i).displayName()+":"+attach.get(i).id());
        }
        System.out.println("请输入需要attach的pid编号");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        VirtualMachineDescriptor virtualMachineDescriptor = attach.get(new Integer(s));
        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(virtualMachineDescriptor.id());
            virtualMachine.loadAgent("/Users/zhangchanglu/IdeaProjects/agent-example/agent-demo/target/java-agent.jar", "param");
            virtualMachine.detach();
        } catch (AttachNotSupportedException e) {
            System.out.println("AttachNotSupportedException：" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException：" + e.getMessage());
        } catch (AgentLoadException e) {
            System.out.println("AgentLoadException：" + e.getMessage());
        } catch (AgentInitializationException e) {
            System.out.println("AgentInitializationException：" + e.getMessage());
        }
    }
}
