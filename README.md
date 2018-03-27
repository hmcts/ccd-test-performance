# Performance Test CCD

## Domains Covered:
1 - Core Case Data

2 - User Profile

## To Run:

- Specify the environment against which to test. Default environment is Dev. The list of supported environments and their required configuration is in src/test/resources/application.conf

    export ENVIRONMENT=<enviroment name>

    e.g: export ENVIRONMENT=cnp

- Depending on the specific environment, open required ssh tunnels (Idam, Bastion host...)
    
- To run all the performance tests:
 
    mvn clean gatling:test
    
- To run a specific simulation
  
    mvn clean gatling:test -Dgatling.simulationClass=<simulation class name>
    
    e.g: mvn clean gatling:test -Dgatling.simulationClass=uk.gov.hmcts.ccd.simulation.CreateCasesSimulation
## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
