package com.example.agentdemoweb;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgentDemoWebApplication {
	/**
	 * 一个线程每隔500毫秒就调用一次Test了的test方法
	 *vm 参数（-javaagent:/Users/zhangchanglu/IdeaProjects/agent-demo/target/java-agent.jar）
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					new Test().test();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
