# Serialization Assignment

All source code can be found in `serialization-assignment/src`

*Bonus code can be found in* `serialization-assignment/src/xml`

## Run Instructions

Run ObjectCreator.java first, which will wait until the Visualizer client joins.

Next, run Visualizer.java, which will connect to the ObjectCreator server socket.

ObjectCreator will spawn a menu that allows you to choose what kind of object to send, the primitive values of that object, and whether or not to send the object as a JSON object or an XML document.

Visualizer will recieve the data over a socket, and after deserializing the object, will display its contents to the console using reflection.

To quit both programs, choose quit (option 6) from the ObjectCreator menu, which will let the client know to quit as well.

## Unit Testing

I have created 4 JUnit test files, to test the outputs of both the JSON and XML serializers and deserializers.

The code was tested using JUnit 5 (Jupiter). Test files can be found in `serialization-assignment/test`.

## Refactoring

### Duplicate Code

I noticed that I had quite a lot of duplicated code in `ObjectCreator.java`. I had a seperate sendObjectX method for each of the 5 objects, when in reality only the creation code was different for each object, and the code to send each object was exactly the same.

`sendObjectA()` and `sendObjectB()` shown below as an example

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

These methods are called based on the user's choice at the menu.

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

In order to deal with this bad code smell, I used the `Extract Method` technique to extract the common functionaility of all 5 functions into one general purpose function `sendObject(Object object)`

```java
private void sendObject(Object object) throws Exception {
	
	System.out.println("Serializing");
	JsonObject json = Serializer.serializeObject(object);
	
	System.out.println("Sending object");
	System.out.println(json.toString());
	
	JsonWriter jsonWriter = Json.createWriter(clientSocket.getOutputStream());
	jsonWriter.writeObject(json);
	System.out.println();
	
}
```

The duplicate code was then taken out of each of the 5 methods, and the `Rename Method` technique was applied in order to better reflect what their new purpose is.

`createObjectA()` shown below as an example

```java
private ObjectA createObjectA() throws Exception {
	
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
	return objectA;
}
```

See commit: `196934ae68f8e968f6e8c488a590698dfca36aef`

The methods are now called from the menu as follows:

```java
switch (choice) {
case 1:
	sendObject(createObjectA());
	break;
case 2:
	sendObject(createObjectB());
	break;
case 3:
	sendObject(createObjectC());
	break;
case 4:
	sendObject(createObjectD());
	break;
case 5:
	sendObject(createObjectE());
	break;
case 6:
	quit = true;
	break;
	
}
```

See commit: `93ea94b7473ca9ecf1bf89b04a3e438b4a10b811`

Not only does this refactoring make the code shorter, more organized and easier to read, but it allows us to add on additional functionaility much more easily. After this refactoring, code to allow the user to choose between JSON and XML serialization needed to be added. If we had not done this refactoring, this code would have had to be added 5 seperate times in each of the `sendObjectX()` functions. Now, this code need only be added once into the `sendObject` method. 
