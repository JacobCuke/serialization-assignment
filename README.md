# CPSC 501 Assignment 3 - Serialization

All source code can be found in `serialization-assignment/src`

*Bonus code can be found in* `serialization-assignment/src/xml`

## Unit Testing

I have created 4 JUnit test files, to test the outputs of both the JSON and XML serializers and deserializers.

The code was tested using JUnit 5 (Jupiter). Test files can be found in `serialization-assignment/test`.

## Refactoring

### Duplicate Code

I noticed that I had quite a lot of duplicated code in `ObjectCreator.java`. I had a seperate sendObjectX method for each of the 5 objects, when in reality it is only the creation code that is different for each object, and the code to send each object is exactly the same.

`sendObjectA()` and `sendObjectB()` below as example

```java
private void sendObjectA() throws Exception {
	
	Scanner scan = getScanner();
	System.out.println("Creating Object...");
	
	System.out.print("Please enter an integer value: ");
	while (!scan.hasNextInt()) {
		scan.next();
		System.out.print("Value must be an integer: ");
	}
	
	int x = scan.nextInt();
	scan.nextLine();
	
	System.out.print("Please enter a float: ");
	while (!scan.hasNextFloat()) {
		scan.next();
		System.out.print("Value must be a float: ");
	}
	
	float y = scan.nextFloat();
	scan.nextLine();
	
	ObjectA objectA = new ObjectA(x, y);
	
	System.out.println("Object Created");
	System.out.println();
	
	System.out.println("Serializing");
	JsonObject json = Serializer.serializeObject(objectA);
	
	// TODO: sendObject(JsonObject json)
	System.out.println("Sending object");
	System.out.println(json.toString());
	sendObject(json);
	System.out.println();
	
}

private void sendObjectB() throws Exception {
	
	Scanner scan = getScanner();
	System.out.println("Creating Object...");
	
	System.out.print("Please enter a boolean value: ");
	while(!scan.hasNextBoolean()) {
		scan.next();
		System.out.print("Please enter true or false: ");
	}
	
	boolean z1 = scan.nextBoolean();
	scan.nextLine();
	
	System.out.print("Please enter another boolean value: ");
	while(!scan.hasNextBoolean()) {
		scan.next();
		System.out.print("Please enter true or false: ");
	}
	
	boolean z2 = scan.nextBoolean();
	scan.nextLine();
	
	ObjectB objectB1 = new ObjectB(z1);
	ObjectB objectB2 = new ObjectB(z2);
	
	objectB1.setOther(objectB2);
	objectB2.setOther(objectB1);
	
	System.out.println("Object Created");
	System.out.println();
	
	System.out.println("Serializing");
	JsonObject json = Serializer.serializeObject(objectB1);
	
	// TODO: sendObject(JsonObject json)
	System.out.println("Sending object");
	System.out.println(json.toString());
	sendObject(json);
	System.out.println();
	
}
```

These methods are called based on the user's choice at the menu

```java				
switch (choice) {
case 1:
    sendObjectA();
    break;
case 2:
    sendObjectB();
    break;
case 3:
    sendObjectC();
    break;
case 4:
    sendObjectD();
    break;
case 5:
    sendObjectE();
    break;
case 6:
    quit = true;
    break;
    
}
```

