# Data Analysis
This program includes validation, parsing and analysis a data file of energy utility meter readings. This is helpful to parse data before load it into the main systems. It will be used to quickly calculate some simple summary statistics and giving context to its contents.

## Data File
* This is a gzipped pipe delimited text file
* Each row represents a meter reading that a utility would use to generate a bill
* ElecOrGas column represents the resource measured: 1=Electricity, 2=Gas
> *NOTE:* A small sample of utility data file is exist in the resources folder but this program is able to analyze large data file as well.

## Statistics Requirements
1. Number of unique customers
2. Breakdown of the number of customers that have electricity only, gas only, and both electricity and gas
3. Breakdown of the number of meter readings per customer per resource
4. Average consumption per bill month per resource

## Instructions
Just run the file `UtilityAnalysis.java`