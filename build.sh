rm -r bin/towerd
javac -p resources/javafx-lib --add-modules javafx.controls src/towerd/*.java -d bin/  && \
cp src/towerd/application.css bin/towerd/ &&\
java -p resources/javafx-lib --add-modules javafx.controls -cp bin/ towerd.GameDriver
