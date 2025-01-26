# Mentoring

멘토링 프로그램 홈페이지
## Development

Update your local database connection in `application.yml` or create your own `application-local.yml` file to override
settings for development.

After starting the application it is accessible under `localhost:8080`.

## Build

The application can be built using the following command:

```
gradlew clean build
```

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./build/libs/Mentoring-0.0.1-SNAPSHOT.jar
```

If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
gradlew bootBuildImage --imageName=pnu.ibe.justice/mentoring
```

## Further readings

1. question answer -> user 명 안 보임. (해결)
2. systemUser 접근이 모든 페이지에서 진행됨 ->시큐리티 설정 바꿔야될 듯 . admin/** 으로 접근할 때만 formlogin 으로 들어가야 됨.
