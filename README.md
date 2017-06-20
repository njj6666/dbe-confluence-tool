#Introduction 
A simple tool with JAVA to help validate DBE Confluence Accelerators.

ARTIFACTS:
Before putting the artifacts to DBE Nesus, we kept them in the repository for now.
User can find the various version of artifacts in directory of /release.

#Releases
## Version 2.0.0
1. Compare page tree between two parent pages.
2. Enhance label service, find out the invalid label name.
3. Enable configuration out-of-source-code.
4. Foster link service, add more exception URL.

## Version 2.0.1
1. Bug fixed - credential configuration of compare service
2. Bug fixed - hard code of base_url in link service

#Command Line
Usage: java -jar dbe-confluence-tool.jar <services> [args]
where services include:
        help          -List the usage.
        checklink     -Validate all href links in confluence and generate a CSV
report.
          -args:
          username              -Your Confluence username
          password              -Your Confluence password
        checklabel    -Find out all the pages without labels and generate a CSV
report.
          -args:
          username              -Your Confluence username
          password              -Your Confluence password
        comparepage     -compare pages between two parent pages, list which page
s are missing, which are redudant
          -args:
          username              -Your Confluence username
          password              -Your Confluence password
See https://atcswa-cr-atlassian.ecs-core.ssn.hp.com/confluence/display/EASE/DBE+Confluence+Tool for more details.