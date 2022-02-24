# Find My Recycling

---

Design Document

Matthew Caudill  
Hamza Rauf    
Daniel Cullen     
Jeffrey Benton     
Bhakti Pokhrel

## Introduction

Do you want to recycle, but don’t know where to take it? Have harsh chemicals that you can’t put in the trash? FindMyRecycling can help you:
- Search for recycling centers near you
- Save centers for easy access  

Use your android device to find recycling centers near you. Save your preferred facilities to your phone!

## [Storyboard](https://projects.invisionapp.com/prototype/FindMyRecycling-ckz0r6vi3000zz501chx8mi2u/play/c01481d8)

![storyboard1](https://user-images.githubusercontent.com/82420942/151686533-b0e34993-3573-4259-a595-410139b7a6d1.png)
![storyboard2](https://user-images.githubusercontent.com/82420942/151686537-c737bdfa-5268-4835-a02b-4b9da4a4c034.png)


## Functional Requirements

## Requirement 100.0 Find my Recycling Center
#### Scenario  

As a user interested in recycling, I want to be able to search for recycling centers based on any part of the recyclable product’s name.  

#### Dependencies  

Recycling center data is available and accessible  

Device has GPS capabilities, and the user has granted access  

#### Assumptions  

Product names are in English  

#### Example  

1.1  

**Given** a feed of product data is available  

**When** I search for “Cell Phone”  

**Then** I should receive at least one result with these attributes   

	A facility to recycle cell phones  
	
1.2  

**Given** a feed of product data is available  

**When** I search “batteries”  

**Then** I should receive at least one result with these attributes  

	A facility to recycle Rechargable Batteries  
  
**And**  

	A facility to recycle Lead Acid Batteries  
  
1.3  

**Given** a feed of product data is available  

**When** I search “human remains”  

**Then** I should receive zero results (an empty list)

## Requirement 101.0: Save my center / save my search  

#### Scenario  

As a user interested in recycling, I want to be able to save my favorite facilities, and my favorite searches.  

#### Dependencies  

Recycling center data is available and accessible  

Device has GPS/address capabilities, and the user has granted access  

User has logged into personal profile  

#### Assumptions  

Product names are in English  

2.1  

**Given** a user wants to save facility  

**When** facility is selected and a user signed in  

**Then** I should be able to do the following  

	You can save a facility to favorites

2.2  

**Given** a user wants to save facility  

**When** there is an active search and a user is signed in  

**Then** I should be able to do the following

	You can save the search to favorites  

## Class Diagram  

![FindMyRecycling draft drawio](https://user-images.githubusercontent.com/82420942/151686286-5f5ee54c-662f-4f67-bfd6-ed5e116f2170.png) 

## Class Diagram Description  

**MainActivity** – The first screen the user sees. This will have the ability to enter search terms for recyclable products and a location to specify where you would like to search for a recycling facility.  
**RetrofitInstance** – Bootstrap class required for Retrofit.  
**Product** – Noun class that represents a recyclable product.  
**Facility** – Noun class that represents a recyclable facility.  
**IProductDAO** - Interface for Retrofit to find and parse product JSON.  
**IFacilityDAO** – Interface for Room to persist facility data.  

## [Product Backlog](https://github.com/Smoofington/findmyrecycling/projects)

## Scrum roles
DevOps/Scrum Master - Matthew Caudill  
UI Specialist(s) - Daniel Cullen, Bhakti Pokhrel  
Integration Specialist(s) - Hamza Rauf, Jeffrey Benton  

## [Github](https://github.com/Smoofington/findmyrecycling)  

## Weekly Meeting  
Tuesday and Saturday at 7pm EST on discord
