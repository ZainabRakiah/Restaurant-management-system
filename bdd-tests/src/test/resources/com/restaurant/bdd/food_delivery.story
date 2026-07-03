Feature: End-to-end food delivery flow

Narrative:
In order to receive meals at home
As a hungry customer
I want to place an order and track delivery until it arrives

Scenario: Customer places order and driver completes delivery
Given the food delivery platform is available
And a customer account exists
And a driver account exists
And a restaurant with menu items is registered
When the customer places an order for one menu item
Then the order should be created with status PLACED
When a driver is assigned to the order
Then the delivery should be marked as ASSIGNED
When the driver completes the delivery
Then the delivery should be marked as DELIVERED
