#!/bin/bash
set -o xtrace
touch ~/.m2 && 
mv ~/.m2 "/tmp/`date`.p2" && 
touch ~/.p2 && 
mv ~/.p2 "/tmp/`date`.m2" && 
touch ~/.eclipse &&  
mv ~/.eclipse "/tmp/`date`.eclipse" &&
touch ~/workspace &&  
mv ~/workspace "/tmp/`date`.workspace" &&
cd ~/Downloads &&
touch eclipse &&
mv eclipse  "/tmp/`date`.eclipse" 
shopt -s extglob # likely already set in interactive shells
tar xvfz eclipse-java-*.tar.gz
eclipse/eclipse & 
