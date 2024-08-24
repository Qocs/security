# security
## Spring security를 학습하기 위한 repository입니다.
> Spring docs 공식 문서를 학습하고 직접 프로젝트에 녹임으로써 체득합니다.

## Spring security

Security가 필요한 이유

단일 페이지(HTML5) 애플리케이션은 의미 있는 상호작용을 위해선 정적 콘텐츠(HTML, CSS, Javascript)뿐만 아니라 백엔드 서버가 필요합니다.

백엔드 서버는 동적 HTML 랜더링과 사용자 인증, 보호된 리소스에 대한 접근 보안, HTTP 및 JSON(REST API)을 통해 브라우저에서 JavaScript와 상호작용하는 등 많은 것을 수행할 수 있습니다. 하나의 애플리케이션은 누구에게나 공용적인 부분도 존재하지만 누구에게나 열려서는 안되는 부분, 그리고 사용자의 요청에서 특정 사용자를 확인하고 보호하는 작업으로 더 안전하고 틈새 없는 애플리케이션을 개발할 수 있기에 Security 기술이 필요합니다.

### Spring security를 적용하기 위한 준비

* 보안을 적용할 애플리케이션이 필요합니다. (Spring initilaizer, STS 등 기호에 맞게 사용)
  
* 보안이 적용되지 않은 간단한 View 2 페이지를 준비합니다. (Thymeleaf 템플릿 사용)
  
* MvcConfig 클래스를 통해 애플리케이션에서 Spring MVC를 구성합니다.
  
  * 웹 애플리케이션은 Spring MVC를 기반으로 합니다. Spring MVC는 애플리케이션의 각 구성 요소를 명확하게 분리하여 관리할 수 있게 해줍니다. 따라서 Spring MVC를 구성하고, 만든 템플릿(view)을 노출하기 위해 뷰 컨트롤러를 설정해야 합니다.
    
    > 여기서 뷰 컨트롤러의 주 역할은 화면 구성 요소들 즉, View를 관리함과 동시에 화면과 데이터(Model) 사이의 상호작용까지 관리합니다. 쉽게 말해 사용자의 요청에 맞는 데이터를 표시할 View를 제공합니다.
    
  * WebMvcConfigurer를 상속하는 MvcConfig 구현체를 생성합시다.
    
    * 여기서 WebMvcConfigurer란?
      
      * 스프링 프레임워크에서 요구사항에 맞게 프레임워크를 조정할 수 있게 해주는 인터페이스입니다.
        
      * @EnableWebMvc를 통해 활성화된 WebMvc 애플리케이션의 구성 정보를 커스터마이징하는 것을 돕습니다.
        
      * 스프링에 추가적인 설정을 해줄 필요가 있을 때 사용합니다.
        
    * 사용 용도
      
      * 인터셉터를 등록합니다. request를 핸들링하기 전 후로 처리할 작업이 있을 때 커스텀 인터셉터를 구성하는 용도로 사용합니다. ex) 글로벌 인터셉터를 등록하여 매번 로깅이나 보안 검사를 할 수 있습니다. 이건 점차 알아가보겠습니다.
        
      * 뷰 리졸버의 동작 과정을 커스터마이징합니다. 뷰 리졸버의 역할인 컨트롤러에서 반환된 뷰 이름이 실제 뷰 오프젝트로 변환되는 과정을 정의할 수 있습니다. 즉, 뷰가 resolve되는 방식을 커스텀한 로직으로 변경합니다.
        
      * 리소스를 핸들링합니다. 정적 리소스 (CSS, image, JavaScript)를 어떻게 제어할지 구성할 수 있습니다. 예를 들어, 리소스의 위치를 적절하게 설정하여 스프링이 효과적으로 리소스를 찾을 수 있도록 합니다.
        
      * 메세지를 변환합니다. HTTP 메시지 컨버터를 추가하여 JSON, XML과 같은 형식의 데이터를 읽고 쓸 수 있는 메세지 변환기를 등록할 수 있습니다.
        
      
      > 저희는 여기서 뷰 리졸버의 동작하는 과정을 커스터마이징하는 기능을 위해 사용합니다.
      
* Spring security 설정
  
  * 만약 허가받지 않은 사용자가 인사말 페이지(/hello)를 보는 것을 방지하고 싶다는 상황을 가정해봅시다. 즉, 로그인을 한 사용자들만이 인사말 페이지를 볼 수 있게 하고 싶다면 /hello url에 로그인을 강제하는 장벽을 추가해야 합니다.
    
  * 장벽을 추가하는 방법은 애플리케이션에서 Spring security를 구성하면 됩니다. 클래스 경로에 Spring security가 있는 경우, Spring Boot는 "기본" 인증으로 모든 HTTP 엔드포인트를 자동으로 보호합니다. 그러나 보안 설정을 추가하며 사용자 지정할 수 있습니다.
    
    * 가장 먼저 해야 할 일은 Spring security를 클래스 경로에 추가하는 것입니다. (build.gradle에 애플리케이션용 한 줄, Thymeleaf & Spring Security 통합용 한 줄, 테스트용 한 줄)
      
    * WebSecurityConfig 구성
      
      * @EnableWebSecurity : 웹 보안 지원을 활성화하고, Spring MVC의 통합을 제공합니다.
        
      * SecurityFilterChain으로 어떤 URL을 보안해야 하는지 메서드를 구성합니다. SecurityFilterChain은 HttpSecurity 객체를 이용하여 기능합니다. 그리고 해당 메서드는 예외를 던집니다.
        
        * authorizeHttpRequests : 인증되지 않은 사용자를 포함해서 누구든지 처음 접근할 수 있는 페이지를 접근 허가시킵니다. permitAll() 모두 접근할 수 있는 페이지를 제외한 나머지 모든 요청은 인증되어야 접근이 가능하도록 구성합니다. anyRequest().authenticated()
          
        * formLogin : 로그인 페이지를 설정합니다. 로그인 페이지 또한 누구나 접근 가능하도록 열어둡니다. permitAlll()
          
        * logout : logout 또한 누구나 접근 가능하도록 설정합니다. (login하여 인증된 사용자만이 logout을 볼 수 있기 때문에 결국 인증된 사용자들 모두는 logout에 접근이 가능합니다.)
          
      * UserDetailsService : 단일 사용자를 저장하기 위한 메모리 내 저장소를 설정하는 메서드입니다.
        
        * Spring Security가 제공하는 UserDetails를 통한 user 객체를 만들어 username, password, roles를 부여합니다. 그러면 해당 사용자에게 3가지가 default값으로 제공됩니다.
