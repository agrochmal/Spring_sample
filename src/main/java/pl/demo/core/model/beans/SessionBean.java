package pl.demo.core.model.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request", proxyMode=org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS)
public class SessionBean{
	
	public SessionBean(){
		System.out.println(this.toString());
	}

	public void f(){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! F():"+this.toString());
	}
}
