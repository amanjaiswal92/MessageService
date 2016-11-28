package com.ail.narad.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ail.narad.factory.IvrCalls;

//@Configuration
//@AutoConfigureAfter(WebConfigurer.class )
@Component
public class IvrConsumerConfiguration implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private IvrCalls ivrCall;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		try {
			ivrCall.consumeThis("IVR_CALLS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * @PostConstruct public void afterConfig() { Thread t = new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { try {
	 * System.out.println("But i will wait"); Thread.sleep(1000 * 60);
	 * System.out.println("I done in thread"); } catch (InterruptedException e)
	 * { e.printStackTrace(); } ivrCall.consumeThis("IVR_CALLS"); } });
	 * System.out.println("Main thread will not wait"); t.start();
	 * System.out.println("I am going"); }
	 */

}
