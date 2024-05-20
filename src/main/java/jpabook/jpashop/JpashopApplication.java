package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
		//hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5JakartaModule;
		// 단순하게 Hibernate5JakartaModule을 new해서 Bean으로 등록하면 Proxy객체를 Lazy Loading하지 않고 무시.
		// 즉 지연 로딩은 무시하게 된다.
	}

}
