var x;
console.log(x); // undefined

var audi = null;
console.log(audi); // null

let userName;
console.log(userName); // undefined
console.log(typeof userName); // undefined

function greet() {
  //no return statement, so it returns undefined
}
console.log(greet());

let x;
x = 10;
console.log(x); // 10

// Null example -------
let profilePicture = null; // explicitly assigned null
console.log(profilePicture); // null
console.log(typeof profilePicture); // object (this is a known quirk in JavaScript)
