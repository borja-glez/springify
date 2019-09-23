# Springify is an open source Java Spring utility library #

[![Build Status](https://travis-ci.org/borja-glez/springify.svg?branch=master)](https://travis-ci.org/borja-glez/springify) [![Maven Central](https://img.shields.io/maven-central/v/com.borjaglez/springify-repository.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.borjaglez%22%20AND%20a:%22springify-repository%22) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Springify Repository is based on https://github.com/ZhongjunTian/spring-repository-plus with some fixes:
* @ManyToMany and OneToMany relationships compatibility.
* Added new filter options like pagination, ignoreCase and order by.
* Added group by to SpecificationBuilder as well as other functions.
* Fixes OneToOne and ManyToOne cross join on hibernate.
* Added interfaces IFilter and IPageFilter to extend filter functionalities (like AnyPageFilter).
* More coming :)

### Use Springify as Maven dependency

Add this to your pom.xml file to use the latest version of Springify Repository:

```xml  
<dependency>
	<groupId>com.borjaglez</groupId>
	<artifactId>springify-repository</artifactId>
	<version>0.3.3</version>
</dependency>
```

Or import the modules independently.

### License

Springify is Open Source software released under the 
[Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/5ae73404dd44141ae78b)