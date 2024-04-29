FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar statistic.jar
ENTRYPOINT ["java","-jar","/statistic.jar"]