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
    
- To run all the performance tests:
    
    ```
    mvn clean gatling:test
    ```
    
- To run a specific simulation
    ```
    mvn clean gatling:test -Dgatling.simulationClass={simulation_class_name}
    ```
    e.g: 
    ```
    mvn clean gatling:test -Dgatling.simulationClass=uk.gov.hmcts.ccd.simulation.CreateCasesSimulation
    ```

- To create a fat jar with all dependencies 

    ```
    mvn package
    ```
    
    then set the environment and run the simulation with: 
    
    ```    
    ./launch.sh {path_to_fat_jar} 
    ```
    e.g.:
    
    ```
    ./launch.sh ./target
    ```
    
## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
