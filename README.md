# react-native-call-record
get call record for react native 

## Install

```
npm i react-native-call-record --save
react-native link react-native-call-record
```

#### getAll

Example
```javascript
import CallRecord from 'react-native-call-record';
CallRecord.getAll({}).then(obj => {
  console.log(obj);
});
```

#### Request Object

| Property        | Type           | Description  |
| ------------- |:-------------:| :-----|
| isDistinct | bool (default false)      | Enable or disable distinct |
| limit      | number(default -1) | limit of result |

#### Response Object

| Property        | Type           | Description  |
| ------------- |:-------------:| :-----|
| phoneNumber   | string | phone |
| duration      | number      | duration |
| name | string      | name |
| dateTime | string | dateTime |
| type | number | type |


## License
*MIT*
