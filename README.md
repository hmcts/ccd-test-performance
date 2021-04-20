# Performance Test CCD

## To Run:

- Specify the environment against which to test. The list of supported environments and their required configuration is in src/test/resources/application.conf

    ```
    export ENVIRONMENT={enviroment_name}
    ```
    e.g: 
    
    ```
    export ENVIRONMENT=cnp
    ```
    
- Depending on the specific environment, open required ssh tunnels (Idam, Bastion host...)
    
- To run all the simulations:
    
    ```
    gradle clean gatlingRun
    ```
    
- To run a specific simulation
    ```
    gradle clean gatlingRun-{simulation_class_name}
    ```
    e.g: 
    ```
    gradle clean gatlingRun-uk.gov.hmcts.ccd.simulation.CreateCasesSimulation
    ```

- To create a fat jar with all dependencies 

    ```
    gradle clean jar
    ```
    
    then set the environment and run the simulation with: 
    
    ```    
    ./launch.sh {path_to_fat_jar} {fully_qualified_simulation_name}
    ```
    e.g.:
    
    ```
    ./launch.sh ./target uk.gov.hmcts.ccd.simulation.UserProfileSimulation
    ```
    
## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.


##### Note
For more information on the gradle gatling plugin please visit https://github.com/lkishalmi/gradle-gatling-plugin

This repo has expired and been replaced by cdm-test-performance
