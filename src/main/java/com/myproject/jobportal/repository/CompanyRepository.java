package com.myproject.jobportal.repository;


//In case if you are looking only for the CRUD operations related methods, then we need to
//extend the CrudRepository or ListCrudepository.

//Since I want to extend all the functionality from the framework, what I'm going to do is
//I will try to extend the interface available from the Spring Data JPA.
//The interface is JpaRepository.

import com.myproject.jobportal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //Optional annotation is completely optional.Even though if you're not mentioning these annotations, still the bean of this interface
//is going to be created during the startup of the application.
//How the framework is going to know to create the bean of these interface.
//Since we are extending the JpaRepository, which is one of the interface from the Spring
//Data JPA or Spring Data project, the Spring and Spring Boot framework, they are going
//to have clear directions behind the scenes to create a bean of these interface during the startup.

/** Yeah, you may have a question, Sachin, bean is nothing but a Java object, but this is an interface,
 * how can we create an object of an interface?
 * This can be a question.
 * Let me answer the same.
 * Before the framework tried to create a bean of this interface, first, what it is going
 * to do is it is going to generate the implementation code for all the abstract methods that we
 * are inheriting from these JpaRepository interface.
 * Once the implementation logic is generated for that implementation class, the bean is
 * going to be created.
 * So all this is internal to the framework.
 * I'm just sharing for your reference.
 * Once this interface is ready, you can inject these interface as a dependency into the controller
 * or service layer.
 * */

//Let's try to do the same.

public interface CompanyRepository extends JpaRepository<Company,Long> {

//@Query("select DISTINCT c, j from Company c join fetch c.jobs j where j.status = :status ") //"When using JPQL, the Persistence Context behaves identically to derived query methods during entity hydration. The Persistence Context is only bypassed during bulk modifying operations (@Modifying UPDATE/DELETE) or when fetching non-entity DTO projections."
// Overrides the inherited JpaRepository.findAll(String status) method to execute custom query logic (e.g., JOIN FETCH, filtering)

//But

// Preferred approach: Use a descriptive method name instead of shadowing findAll()
//@Query("SELECT DISTINCT c FROM Company c JOIN FETCH c.jobs j WHERE j.status = :status")
//	List<Company> findAllWithJobsByStatus(@Param(value = "status")String status);

//Lets see native SQL too

@Query(value = """
       SELECT DISTINCT c.* FROM companies c
       JOIN jobs j ON c.id = j.company_id
       WHERE j.status = :status
       """, nativeQuery = true)
List<Company> findAllWithJobsByStatus(@Param("status") String status);

/**
 * ISSUES WITH THIS NATIVE QUERY:
 *
 * 1. N+1 SELECT PROBLEM:
 *    Because this query only selects 'c.*', Hibernate loads Company entities with uninitialized
 *    (LAZY) 'jobs' collections. Iterating through 'company.getJobs()' will trigger 1 additional
 *    SQL query per Company to load its child jobs.
 *
 * 2. UNFILTERED CHILD COLLECTION (CLOSED JOBS ARE LOADED):
 *    The 'WHERE j.status = :status' clause only filters WHICH Companies are returned, but not WHICH Jobs are returned.
 *    When Hibernate later initializes 'company.getJobs()' via lazy loading, it fetches ALL jobs
 *    belonging to that company from the database, including 'CLOSED' ones.
 *    [Solution: use @SQLRestriction("parameter = 'Value'") i.e. @SQLRestriction("status = 'ACTIVE'") on top of the column name in the parent entity definition i.e. Company]
 *
 * 3. DUPLICATE ENTITIES:
 *    If a company has 3 ACTIVE jobs, the SQL JOIN returns 3 duplicate company rows.
 *    Adding 'DISTINCT c.*' (or handling it at the entity level) is required to avoid duplicate Company objects.
 */


//@Query(value = """
//		SELECT c.*,j.* FROM companies c // it have same id alias for jobs and companies orimary key (id) so error will be thrown at runtime
//		    JOIN jobs j
//		        on c.id = j.company_id
//		           WHERE j.status = ?1""", nativeQuery = true)
//	List<Company> findAllWithJobsByStatus(@Param(value = "status")String status);

}
