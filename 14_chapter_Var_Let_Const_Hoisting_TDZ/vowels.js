let str = "Hello World";

let vowels = 0;
let consonants = 0;

for (let i = 0; i < str.length; i++) {
  let ch = str[i].toLowerCase();

  if (ch >= "a" && ch <= "z") {
    if ("aeiou".includes(ch)) {
      vowels++;
    } else {
      consonants++;
    }
  }
}

console.log("Vowels:", vowels);
console.log("Consonants:", consonants);
