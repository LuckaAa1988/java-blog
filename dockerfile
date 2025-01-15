FROM tomcat:10.1-jdk21

COPY target/java-blog.war /usr/local/tomcat/webapps/ROOT.war

COPY server.xml /usr/local/tomcat/conf/server.xml
COPY uploads/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg /usr/local/tomcat/uploads/2e92c3f2-3d76-45b2-b651-7a14d7231a60.jpg

CMD ["catalina.sh", "run"]