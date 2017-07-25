== Generate properties Maven Plugin ==

	The objective of this plugin is to compare the files properties in local post dev with the properties 
	in deployment floder and replace them by the new properties files

== Example == 

	Here a quick example of what this plugin does
	Here is example of file .properties in my local
		----------------------------------------------
			database.jdbc.username= root
			database.jdbc.password=
			database.hibernate.hbm2ddl.auto=update
		----------------------------------------------
		
	in destination (deployment floder) an file with the same name will be created on phase validate
	of maven lifecycle

		------------------------------------------------------------------------------
			database.jdbc.username={{ database.jdbc.username }}
			database.jdbc.password={{ database.jdbc.password }}
			database.hibernate.hbm2ddl.auto={{ database.hibernate.hbm2ddl.auto }}
		-------------------------------------------------------------------------------
	
== Configuration ==

	Required -->
		origin : the PATH to the local floder of properties files
		destination : the PATH of the deployment floder or any floder you want to copy your properties in
		
	Option --> 
		enableComment : boolean by default true
						to copy the comment in the new files
		comment : String by default "#"
				  character to identify the comments line
		separator : String by defaut "="
					the character to separate the keys for values 
		openValueSeparator : String by default "{{ "
		closeValueSeparator : String by default "}} "

== instalation ==

	you have to clone the project in run mvn clean install don't forget to replace the <groupId>
	in the pom.xml file
	
== Example of pom.xml file of an example project ==

		<plugins>
            <plugin>
                <groupId>com.ismail963.maven.plugin</groupId>
                <artifactId>compare-properties-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>

                <configuration>
                    <origin>path/to/origin/folder</origin>
                    <destination>path/to/destination/folder</destination>
					<enableComment>true</enableComment>
                </configuration>

                <executions>
                    <execution>
                        <id>compare-validation</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>compare-clean</id>
                        <phase>clean</phase>
                        <goals>
                            <goal>clean</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
		<plugins>	
