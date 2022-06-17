<h1>**Canteen E-Queue System**<h1>

**Important Links:**

Runnable exe: https://drive.google.com/file/d/1YC9roUIw5kkvdhs7pG8cuvzElt1dAkk6/view?usp=sharing

Video demonstration: https://youtu.be/tK2E5WxZY3c

GitHub project link: https://github.com/ter-bino/E-Queue-System


**Details:**

An interactive software system, to assist a small eatery like a canteen in queueing orders.

Inspired by self-service kiosks in fast food restaurants.

The system consists of three applications:

Server App

Client App

Announcer App

![image](https://user-images.githubusercontent.com/93169758/174222433-be8ec2c9-48c3-4788-877d-9864e970a931.png)


**Lunching the System:**

Among the 3 applications, Server App MUST be lunched first. Upon lunching ServerApp, it will show you the Local IP address where it hosts the server. If no LAN connection is detected, then the server is opened on the localhost of the device.

![image](https://user-images.githubusercontent.com/93169758/174222453-0f0e00a2-c18a-4a71-beed-c7c87ca189f2.png)


You need to remember the local IP address shown to you by the ServerApp, because Announcer app and the multiple client apps will ask for the IP address of the server.

![image](https://user-images.githubusercontent.com/93169758/174222479-088fe40e-ec61-423e-b257-017971abd0d4.png)

 
**Server App:**

You can switch between the two views with the buttons "FULL MENU" and "ORDER LIST" in the upper left of the screen which shows the list of items in the menu and the list of orders from clients, respectively. Each view also comes with their own functionalities (buttons)

![image](https://user-images.githubusercontent.com/93169758/174222503-a574dc1c-a085-4174-b599-0f83627e1003.png)


In the "FULL MENU" view, you will have filter buttons in the lower left to show items in the menu per category. At the bottom of the view, add an item, remove an item, or edit an item from the displayed menu. You can also edit/remove an item by clicking on them directly in the menu, to open a "Remove/Edit Dialog".

![image](https://user-images.githubusercontent.com/93169758/174222519-c0d59f02-6bc8-4799-b886-78b490f92649.png)


In the "ORDER LIST" view, you will have filter buttons in the lower left to filter orders in the list according to their status. At the bottom of the view, you have options to refresh the list of orders, change the status of an order to processing, serving or completed by entering order numbers. You can also see full details of the order by clicking them directly. It opens the "Order Details Dialog", which has a button to proceed the status of your order.

![image](https://user-images.githubusercontent.com/93169758/174222541-4b4a7f32-78b0-42e6-b84c-5211a5ca3451.png)

**Client App:**

In the Client App, the main content of the GUI is the updated menu constantly fetched from the server. On the upper left part of the GUI, it has the buttons to filter the menu according to categories. It also has a simple instruction of use on the lower left.

At the bottom left of the screen, it has the button to start an order, which will ask for the name of the customer and create their order (similar to a cart in e-commerce).

![image](https://user-images.githubusercontent.com/93169758/174222568-39ef4ce6-05f6-47b6-9fd7-4caded9a975e.png)

Once a customer started an order with their name, they will be allowed to modify the contents of their order by adding or removing items in it. They can add an item to their order by clicking directly in the item that they want to add shown in the GUI, which opens the "Add an Item Dialog". This dialog will ask them to input the amount of this item they want to add in their order.

![image](https://user-images.githubusercontent.com/93169758/174222604-71fe0505-7083-4856-ba75-a628b89f8c3d.png)

Before the customer sends their order, they can check the contents of their order by clicking on the "Check Order" button. This will show them the list of items in their order and the total cost.

![image](https://user-images.githubusercontent.com/93169758/174222631-5a2a0420-04ab-4493-a9cb-34af5ed488fa.png)
 
Once the customer sends their order to the server, the server will send back the order number that it assigned to the customer's order. The dialog that shows up asks the customer to remember their order number.

![image](https://user-images.githubusercontent.com/93169758/174222654-52a5b3f3-ff87-47b8-b15e-1baa89009b86.png)

**Announcer App:**

There really is not much done in the Announcer App. This is just the application that should be displayed on a big screen to let customers know if their order's number is ready to be paid and claimed. Then, this app just waits for the server to send updates on the status of order numbers.

![image](https://user-images.githubusercontent.com/93169758/174222670-b87fc29f-87c7-4e90-9450-3cb3db4eb757.png)

**Summary of Program Use:**

ServerApp: Should be used by the workers of the canteen, to know which orders should they prepare, and make this app send updates to order numbers in the announcer app. Workers should update the menu, update order statuses, and prepare the contents of orders receive from client apps.

ClientApp: Should be ran in the self-service devices around the canteen or eatery, and be used by customers to create their orders and get an order number. The customer should wait for their order number to show in the "serving" section of the announcer, before claiming and paying their order.

AnnouncerApp: Should be ran in a big screen, easily visible for customers. Shows which orders are being processed and which orders are ready for claim and pay.

**Things to Note:**

Instances:
- Only 1 server app should run or be used.<br>
- Only 1 announcer app can connect to the server.<br>
- Multiple client apps can connect to the server.<br>

Note that files "menu.obj" and "item.dat" are important files that should be kept together in the same folder with "ServerApp.exe". Those files keep the menu, and tracks the last ItemIDs assigned to newly added items.

![image](https://user-images.githubusercontent.com/93169758/174222693-a5258625-a9c1-4162-a2a3-c64a35ec2847.png)

