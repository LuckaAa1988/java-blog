FROM tomcat:10.1-jdk21

COPY target/java-blog.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]