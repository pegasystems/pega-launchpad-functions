export const handler = async (event) => {
  const num1 = parseFloat(event.num1) || 0;
  const num2 = parseFloat(event.num2) || 1;

  const addition = num1 + num2;
  const subtraction = num1 - num2;
  const multiplication = num1 * num2;
  const division = num2 !== 0 ? num1 / num2 : 'undefined';

  // Return the results
  const response = {
      statusCode: 200,
      body: JSON.stringify({
          addition: addition,
          subtraction: subtraction,
          multiplication: multiplication,
          division: division
      }),
  };
  return response;
};
