# Spring Hateoas

## Index
1. me.strong.hateoas.person 패키지에서 다루는 내용    
    * EntityLinks
    * ControllerLinkBuilder
    * ResourceSupport
    * ResourceSupportAssembler
    
2. me.strong.hateoas.member 패키지에서 다루는 내용
    * HAL media type
    * Spring Data Jpa의 Pageable, Page
    * PagedResources
    * Nested DTO model
     
     
## RESTful API 개념
* REST, Representational State Transfer는 자원을 이름으로 구분하여 해당 자원의 상태 정보를 주고받는 모든 것을 의미  
즉, 자원(Resource)의 표현(Representation)에 의한 상태(State) 전달이며 여기서 자원은 문서, 그림, 데이터 등을 말한다.  

* WWW과 같은 분산 하이퍼미디어 시스템을 위한 SW 개발 아키텍처의 한 형식으로 ROA(Resource Oriented Architecture)의 한 종류
  
* HTTP URI를 통해 Resource를 명시하고 HTTP Method를 통해 해당 Resource에 대한 Operation을 적용하는 것을 의미한다.
  
각 Http Method에 대한 Operation 및 멱등성에 대한 정리는 아래와 같다.

  | HTTP Method | Operation   | Idempotent |
  |-------------|-------------|------------|
  |     POST    |    Create   |     No     |
  |     GET     |    Select   |     Yes    |
  |     PUT     | Update(전체)|     Yes    |
  |    PATCH    | Update(일부)|     Yes    |
  |    DELETE   |    Delete   |     Yes    |
  

POST 메소드를 이용한 연산은 리소스를 추가하는 것이므로 멱등성이 성립하지 않는다. 따라서 해당 API 호출 시 실패할 경우  
기존에 상태를 다시 원복해주어야 한다. 그 외의 연산은 그저 재수행하기만 하면 된다.  

## RESTful API 특성
* 유니폼 인터페이스  
REST는 HTTP 표준만 잘 따르면 이기종간에 통신에도 문제없는 인터페이스이다. 예를들어 JSON을 기반으로 하는 HTTP API를 정의하면  
Android, IOS, Java, Python 등 특정 언어나 기술에 종속받지 않는 모든 플랫폼에서 사용 가능한 Loosely coupling 형태가 된다

* 무상태성 (Stateless)  
REST는 상태가 없다. 즉 사용자나 클라이언트의 Context를 서버쪽에 유지하지 않는다. 대표적으로 HTTP Session과 같은 것들을  
사용하지 않음을 의미한다. 이것은 상태정보를 저장하는 방법이 플랫폼마다 다른 부분이 많아 종속성을 가지게 됨을 방어하는 것과  
HTTP API는 원래 요청시에 Connection을 맺고 요청이 끝난 경우엔 Connection을 끊어버리는 특성에 기반하기 때문이다.  

* 캐싱 가능 (Cacheable)  
REST는 HTTP 프로토콜을 그대로 사용하기 때문에 기존의 인프라를 그대로 사용 가능하다. HTTP 프로토콜 기반의 LB나 SSL 그리고  
HTTP API의 캐싱기능을 적용할 수 있다. 대부분의 서비스에서 Select와 같은 조회성 API를 호출하는 것을 감안하면  
HTTP 캐싱은 성능 면에서 많은 장점이 있다. 

* 자체 표현(Self-descriptiveness)  
RESTful API는 매우 단순하고 직관적이어서 그 자체만 보고도 이해할수 있는 Self-descriptiveness 구조를 갖는다.  
Resource URI와 메소드를 이용해서 어떤 Resource에 무슨 행위를 하는 지 알 수 있으며 Resource 그 자체가 자신에 대한 설명을  
담는 구조이어서 별도의 문서 없이 API 호출 자체로 이해할 수 있다.

* Client-Server 구조  
클라이언트와 서버를 나누어 각각의 역할을 확실히 하고 서로간의 의존성을 줄일 수 있다.

* 계층형 구조  
다중 계층으로 구성 가능하며 보안, 로드밸런싱, 암호화 계층을 추가하여   
구조상의 유연성을 두거나 Proxy, Gateway 등을 통해 중간 매체 이용 가능
 
 
## RESTful API 디자인 가이드  
* 슬래시 구분자(/)는 계층 관계를 나타내는데 사용한다  
/teams/10/members는 10번 team의 member 목록을 요청하는데 사용한다.

* URI 마지막 문자로 슬래시(/)를 포함하지 않는다  
/users/와 같이 사용하지 말아라
  
* 하이픈(-)은 가독성을 높이는데 사용한다  
긴 이름의 Resource는 하이픈을 통해 구분한다 

* 밑줄(_)은 사용하지 않는다  
밑줄은 가독성을 떨어뜨린다.

*  URI 경로에는 소문자를 사용한다  
/Users/10/Firends와 같은 형태는 가독성을 떨어뜨린다.

*  확장자는 사용하지 않는다.  
반환하는 Resource가 json일지라도 /users.json은 사용하지 않는다.  
반환하는 포맷은 Content-Type을 통해 표현한다

*  모든 Resource는 복수형태로 표현한다  
/user/10이 아닌 /users/10 형태로 표현한다
 
*  URI에 액션에 대한 정보를 담지 않는다  
/users/add와 같이 생성이랑는 액션을 담지 않는다.  
[POST] /users 와 같이 HTTP Method로도 충분하다

* HTTP Header를 적절히 사용한다  
Locale, Content-Type 등을 표현할 때 흔히 /home?locale=en, /users.json와 같이 query parameter로 넘기는 경우가 많다  
하지만 이것은 각각 Accpet-Language, Content-Type 헤더로 표현할 수 있다.  
<a href="https://github.com/dlxotn216/LocaleResolver">Spring LocaleResolver 참고</a>



*  HTTP Status code를 적절히 사용한다  

| HTTP Status Code |               의미              |
|:----------------:|:-------------------------------:|
|        2xx       |               성공              |
|        200       |               성공              |
|        201       |              작성됨             |
|        204       |           Content 없음          |
|        30x       |         리다이렉션 완료         |
|        301       |             영구이동            |
|        302       |             임시이동            |
|        304       |           Not modified          |
|        4xx       |             요청오류            |
|        400       |           잘못된 요청           |
|        401       |            권한 없음            |
|        402       |            결제 필요            |
|        403       |              금지됨             |
|        404       |            Not found            |
|        405       |        Method not allowed       |
|        406       |          Not acceptable         |
|        407       |         Proxy 인증 필요         |
|        408       |          요청시간 초과          |
|        415       |      Unsupported Media Type     |
|        5xx       |             서버오류            |
|        500       |      Internal Server Error      |
|        501       |          구현되지 않음          |
|        502       |           Bad gateway           |
|        503       | Service Temporarily Unavailable |
|        504       |         Gateway timeout         |
|        505       |    HTTP 버전을 지원하지 않음    |  

* 버전관리  
/api/v1/users와 같이 버저닝이 되어야 한다

* 페이징  
많은 사이즈의 리스트를 응답 처리하기 위해서 페이징 처리와 partial response 처리가 필요하다.  
 
* Partial Response  
리소스에 대한 응답으로 굳이 모든 필드를 명시할 필요가 없는 케이스가 있을 것이다.  
예를 들어 사용자의 목록을 Dropdown에 표시하는 경우엔 표시할 사용자이름, 사용자 Key, 사용자 ID 등의 정보만 필요할 것이다.  
그런데 사용자의 주소, 이메일, 나이 등 사용하지 않는 필드들을 함께 반환하는 것은 네크워크 대역폭에 대한 낭비이며  
특히나 제약적인 모바일 환경에선 큰 단점이 된다. 아래와 같이 필드를 명시하는 방법이 있다.   

    * Linked in : /people:(id,first-name,last-name,industry)
    * Facebook : /terry/friends?fields=id,name
    * Google : ?fields=title,media:group(media:thumnail)
 
 
* 검색
    * 전역 검색  
    /users?region=seoul과 같이 검색하면 서울지역에 거주하는 사용자이다
    
    * 지역검색
    /users?region=seoul&page=0&size=10과 같이 검색하면 page, size가 검색조건인지 페이징 조건인지 구별이 힘들다  
    따라서 /users?q=region=seoul,name=Lee&page=0&size=10과 같이 Query string을 delimiter(,)로 구분하는 방법도 있다.
    

* Self-descriptive message  
메시지는 그 자체가 설명이 되어야한다.  
아래 응답 메시지는 그 자체가 성공 반환이며 프로토콜과 응답내용은 json임을 포함한다 
  > HTTP/1.1  200 OK  
  > Content-Type : application/json
  > [{"id" : "Goto home", "title": "I want to go home"}]  

  HTTP 명세에 media type은 IANA에 등록되어있고 application/json에 대한 명세가 있으므로 파싱은 성공한다.  
  하지만 id가 뭔지, title이 무엇을 의미하는 지 알 수 없다.

  (1) Media type을 매번 정의하여 IANA에 등록하여 각 필드에 대한 설명을 단다  
      ->  매번 media type 정의해서 등록해야 하는 귀찮음  
  (2) profile을 Link 헤더에 정보가 담긴 문서를 명세한다  
      ->  Spring rest docs 등을 통해 만들어진 문서를 링크할 수 있다  
    
  참고로 HTML문서의 경우 test/html media type이 IANA에 정의되어있고 각 태그가 잘 설명되어있어 위를 만족한다

* HATEOAS  
  HATEOS는 Hypermedia as the engine of application state의 약어로,    
  하이퍼미디어의 특징을 이용하여 HTTP Response에 다음 Action이나 관계되는 리소스에 대한 HTTP Link를 함께 리턴하는 것이다.    
    
  아래 메시지는 상태에 대한 정의가 없다. 
  > HTTP/1.1  200 OK  
  > Content-Type : application/json  
  > [{"id" : "Goto home", "title": "I want to go home"}]
    
  마찬가지로 Link header를 통해 처리할 수 있다.  
   > HTTP/1.1  200 OK  
   > Content-Type : application/json  
   > Link: </articles/1>; rel="previous;  
   > Link: </articles/3>; rel="next;  
   > [{"id" : "Goto home", "title": "I want to go home"}]  
   
  참고로 HTML문서의 경우 a 태그를 통해 다음 상태로 전이 되므로 상태전이성을 만족한다.
  
  

※ HAL vs ATOM <a href="https://sookocheff.com/post/api/on-choosing-a-hypermedia-format/">참고 레퍼런스</a>
  
단일 리소스에 대해서 요청을 진행 할 경우 하나의 Resource에 대한 상태를 반환한다  

GET https://api.example.com/player/1234567890을 통해 요청 시  
HAL에서는 아래와 같이 표현한다.
```json
{  
      "_links": {  
          "self": { "href": "https://api.example.com/player/1234567890" },
          "friends": { "href": "https://api.example.com/player/1234567890/friends" }
      },  
      "playerId": "1234567890",  
      "name": "Kevin Sookocheff",  
      "alternateName": "soofaloofa",  
      "image": "https://api.example.com/player/1234567890/avatar.png"  
  }  
```

반면 ATOM에선 아래와 같이 표현된다  
```json
{
  "result": {
    "personKey": 1,
    "name": "person1",
    "birth": "2018-06-19T21:41:52.053+09:00",
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/people/1",
        "hreflang": null,
        "media": null,
        "title": null,
        "type": null,
        "deprecation": null
      }
    ]
  },
  "message": "Success",
  "errorCode": null
}
```


목록에 대한 요청을 진행할 경우 전체 Player 자원 목록이 반환되는데 이 각각은 Embedded Resource로 표현에 포함된다.  

GET https://api.example.com/player/1234567890/friends을 통해 요청 시  
HAL에선 아래와 같이 표현한다  
  
```json
{
    "_links": {
        "self": { "href": "https://api.example.com/player/1234567890/friends" },
        "next": { "href": "https://api.example.com/player/1234567890/friends?page=2" }
    },
    "size": "2",
    "_embedded": {
        "player": [
            {
                "_links": {
                    "self": { "href": "https://api.example.com/player/1895638109" },
                    "friends": { "href": "https://api.example.com/player/1895638109/friends" }
                },
                "playerId": "1895638109",
                "name": "Sheldon Dong",
                "alternateName": "sdong",
                "image": "https://api.example.com/player/1895638109/avatar.png"
            },
            {
                "_links": {
                    "self": { "href": "https://api.example.com/player/8371023509" },
                    "friends": { "href": "https://api.example.com/player/8371023509/friends" }
                },
                "playerId": "8371023509",
                "name": "Martin Liu",
                "alternateName": "mliu",
                "image": "https://api.example.com/player/8371023509/avatar.png"
            }
        ]
    }
} 
```

Template 명시  
템플릿은 화면에 대한 정의를 포함하는 개념으로 생각된다.  
아래가 한 예인데 template 객체를 통해 화면에 나타날 라벨, id값, required 여부 등을 전달할 수 있을 것 같다
```json
{
    "collection":
    {
        "version": "1.0",
        "href": "https://api.example.com/player/1234567890/friends",
        "links": [
            {"rel": "next", "href": "https://api.example.com/player/1234567890/friends?page=2"}
        ],
        "items": [
            {
                "href": "https://api.example.com/player/1895638109",
                "data": [
                      {"name": "playerId", "value": "1895638109", "prompt": "Identifier"},
                      {"name": "name", "value": "Sheldon Dong", "prompt": "Full Name"},
                      {"name": "alternateName", "value": "sdong", "prompt": "Alias"}
                ],
                "links": [
                    {"rel": "image", "href": "https://api.example.com/player/1895638109/avatar.png", "prompt": "Avatar", "render": "image" },
                    {"rel": "friends", "href": "https://api.example.com/player/1895638109/friends", "prompt": "Friends" }
                ]
            },
            {
                "href": "https://api.example.com/player/8371023509",
                "data": [
                      {"name": "playerId", "value": "8371023509", "prompt": "Identifier"},
                      {"name": "name", "value": "Martin Liu", "prompt": "Full Name"},
                      {"name": "alternateName", "value": "mliu", "prompt": "Alias"}
                ],
                "links": [
                    {"rel": "image", "href": "https://api.example.com/player/8371023509/avatar.png", "prompt": "Avatar", "render": "image" },
                    {"rel": "friends", "href": "https://api.example.com/player/8371023509/friends", "prompt": "Friends" }
                ]
            }
        ],
        "template": {
            "data": [
                {"name": "playerId", "value": "", "prompt": "Identifier"},
                {"name": "name", "value": "", "prompt": "Full Name"},
                {"name": "alternateName", "value": "", "prompt": "Alias"},
                {"name": "image", "value": "", "prompt": "Avatar"}
            ]
        }

    }
}
```

반면 ATOM에선 아래와 같이 표현된다
```json
{
  "result": [
    {
      "personKey": 1,
      "name": "person1",
      "birth": "2018-06-19T21:41:52.053+09:00",
      "links": [
        {
          "rel": "self",
          "href": "http://localhost:8080/people/1",
          "hreflang": null,
          "media": null,
          "title": null,
          "type": null,
          "deprecation": null
        },
        {
          "rel": "people",
          "href": "http://localhost:8080",
          "hreflang": "en",
          "media": "application/json",
          "title": null,
          "type": null,
          "deprecation": null
        }
      ]
    },
    {
      "personKey": 2,
      "name": "person2",
      "birth": "2018-06-19T21:41:52.053+09:00",
      "links": [
        {
          "rel": "self",
          "href": "http://localhost:8080/people/2",
          "hreflang": null,
          "media": null,
          "title": null,
          "type": null,
          "deprecation": null
        },
        {
          "rel": "people",
          "href": "http://localhost:8080",
          "hreflang": "en",
          "media": "application/json",
          "title": null,
          "type": null,
          "deprecation": null
        }
      ]
    },
    {
      "personKey": 3,
      "name": "person3",
      "birth": "2018-06-19T21:41:52.053+09:00",
      "links": [
        {
          "rel": "self",
          "href": "http://localhost:8080/people/3",
          "hreflang": null,
          "media": null,
          "title": null,
          "type": null,
          "deprecation": null
        },
        {
          "rel": "people",
          "href": "http://localhost:8080",
          "hreflang": "en",
          "media": "application/json",
          "title": null,
          "type": null,
          "deprecation": null
        }
      ]
    }
  ],
  "message": "Success",
  "errorCode": null
}
```

## Spring hateoas  
<a href="https://docs.spring.io/spring-hateoas/docs/current/reference/html/">스프링 공식 레퍼런스</a>

Spring Hateoas에서 HAL 스타일의 Link를 생성할 때 주의점은 ResourceSupport 클래스를 상속한 클래스가 응답 유형이어야 한다는 것  
예상으로 Response의 Content-Type이 application/hal+json이면 알아서 되지 않을까 했지만 동작하지 않았다.  
목록형태를 반환할 때는 PagedResource로 감싸서 반환해야함을 주의해야한다.  

이러한 동작은 Spring hateaos 구현체의 기본 스타일이 HAL이기 때문이며 이것을 따르고 싶지 않다면  
PagedResource, Resource, ResourceSupport를 상속하지 않는 타입을 반환하거나
아래 옵션을 활성화 하고 
```properties
spring.hateoas.use-hal-as-default-json-media-type=false
```
 Response의 Content-type을 application/json으로 명시한다
 ```java
@GetMapping("/members/{memberKey}")
public ResponseEntity<MemberDto.ResourceSupportedApiResponse> searchMember(@PathVariable("memberKey") Long memberKey) {
    return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberDto.ResourceSupportedApiResponse("success", this.memberSearchService.searchMember(memberKey)));
}
```

아래는 그 응답이다
```json
{
  "message": "success",
  "result": {
    "memberKey": 1,
    "memberId": "taesu1",
    "name": "Lee Teae Su 1",
    "joinedAt": "2019-02-28",
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/members/1",
        "hreflang": null,
        "media": null,
        "title": null,
        "type": null,
        "deprecation": null
      }
    ]
  },
  "links": []
}
```


## 마무리
RESTful API의 스펙을 유지하기는 매우 힘들다. RESTful API의 창시자인 로이필딩은 아래와 같이 말한다 
> 시스템 전체를 통제할 수 있다고 생각하거나 진화에 관심이 없다면
> REST에 대해 따지느라 시간을 낭비하지말 것
  
이는 개발 일정으로 인해 합리화 하라는 소리는 아닌 것 같다. 긴 시간에 걸쳐 진화하는 웹 애플리케이션을  
개발한다면 REST를 따라 개발하는 것이 올다는 주장같다.  

특히나 template과 관련된 부분은 front-back-end 구조로 나뉜 상태에서 다국어라든지 필수입력 필드라든지  
서로 나누어서 개발 될   수이 있는 것을 API에서 통합하여 제어할 수 있을 것 같다.  
그 예로는 서버측에서 필수 입력 필드가 아닌것으로 바뀌었는데 화면에서는 해당 필드를   
필수 입력 필드로 처리하게 되는 상황을 들 수 있다 
 
지금껏 resource URI와 HTTP Method 그리고 헤더나 응답값 정도만 잘 지키고  
HATEOAS와 같은 부분에 대해서는 모르는 척 해왔으나 Spring의 구현체를 통해 HATEOAS를 실제 구현할 수 있는 방법을    
모색해본 것이 의미있다고 생각한다.   
또한 여기서 다루지는 않지만 각 응답에 존재하는 Field에 대한 설명을 담아야 한다는 규약인 Self-descriptive는   
Spring Rest Docs를 통해 해결할 수 있을 것으로 생각한다.  
<a href="https://github.com/dlxotn216/springrestdocs">Spring Rest Docs Repository 이동 </a>


