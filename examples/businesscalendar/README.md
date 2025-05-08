# Business Calendar Function

<!-- TOC -->
* [NodeJS Functions](#nodejs-functions)
  * [Calculator function](#calculator-function)
<!-- TOC -->

## Business Calendar Function

This function calculates the number of days (working days, weekends, holidays, etc.) between two dates. It is based on the business-days-js library. It is mainly used for the US, but with modifications to the library, it can also support other countries

### Business Calendar code info code info

- **File**: `calculateBusinessDays.mjs`
- **Function**: handler

### Function rule configuration

- **Runtime**: NodeJS (e.g., 20)
- **Function handler**: calculateBusinessDays.handler
- **Input parameters**:
  - **startDate \(\Date\)**: start date 
  - **endDate \(\Date\)**: end date
  - **state \(\Text\)**: state (optional,US: two-letter state abbreviation)
  - **excludeHolidays \(\Text\)**:excludeHolidays (optional, names of holidays separated by commas that you want to exclude, for example: Juneteenth, Independence Day)
  - **customHolidays \(\Text\)**:customHolidays (optional, names of custom holidays provided in the following way: MM-DD,NameOfHoliday for example: 06-01,Holiday1;07-01,Holiday2)
  - 
- **Output parameters**:
  - **Type**: Text

### Deployment

Create zip file.  

In Pega Launchpad:

1. Select **Runtime**: NodeJS  
2. Upload the zip file to **Code bundle**  
3. Set **Function handler** to `calculateBusinessDays.handler`  
4. Configure the input and output parameters as described above
