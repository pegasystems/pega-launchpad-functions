import businessDays from "business-days-js";

export const handler = async (event) => {

    
    const startDate = new Date(event.startDate);
    const endDate = new Date(event.endDate);
    const state = event.state;
    const excludeHolidays = event.excludeHolidays ? event.excludeHolidays.split(',') : [];
    const Custom_holidays = event.customHolidays
        ? event.customHolidays.split(';').filter(Boolean).map(holiday => {
            const [rule, name] = holiday.split(',');
            return { rule: rule.trim(), name: name.trim() };
        })
        : [];
     
    let bDays;
        if (!state) {
            bDays = businessDays({ excludeHolidays: excludeHolidays, addHolidays: Custom_holidays });
        } else {
            bDays = businessDays({ state: state, excludeHolidays: excludeHolidays, addHolidays: Custom_holidays });
        }
    const result = bDays.countDays(startDate.toISOString().split('T')[0], endDate.toISOString().split('T')[0]);


    const response = {
        statusCode: 200,
        body: JSON.stringify(result, null, 2), // Use JSON.stringify to format the response body
    };
    return response;
  };
  
