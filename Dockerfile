FROM openjdk:8
VOLUME /tmp
ADD ./target/servicioCuentaCorrienteVIP-0.0.1-SNAPSHOT.jar servicioCuentaCorrienteVIP.jar
ENTRYPOINT ["java","-jar","/servicioCuentaCorrienteVIP.jar"]