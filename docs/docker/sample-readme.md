
![Logo](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/th5xamgrr6se0x5ro4g6.png)

# customer-request-br

This API allows for the create and list requests of user.

## Documentação

### **Relevant definitions and concepts**

- **Request:** A request represents a user request or issue that needs to be addressed. Each request is identified by a unique ID and contains information such as the reason for the request, its current status, and any messages related to the request.

- **Request Status:** Represents the current state of a request. Possible status include "awaiting_response", "in progress", "completed", "canceled", and "expired". The status of a request is updated as it moves through the process of resolution.

- **Pagination:** To manage large sets of requests, the API supports pagination through "page" and "per_page" query parameters. This allows clients to fetch a subset of requests at a time.

- **Protocol Number:** A unique identifier assigned to a request upon creation. It can be used for tracking and reference purposes.

### **API functionality**

The API has four endpoints:
- One to return a tree of reasons to which the customer can open requests;
- One to create a new request;
- One to list all requests made by a specific user;
- One to get details of a specific request;


## Uso/Exemplos

```javascript
import Component from 'my-project'

function App() {
  return <Component />
}
```

```bash
# just list all customers
curl http://localhost:8080/api/customers/v1
```


## Technologies Stack

**front-end:** React, Redux, TailwindCSS

**back-end:** Node, Express


## Licences

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)

