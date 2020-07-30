import React, {Component} from 'react';
import {
    PermissionsAndroid,
    Platform,
    SafeAreaView,
    ScrollView,
    StyleSheet,
    Text,
    View
} from 'react-native';

import CallLogs from 'react-native-call-record';

export default class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list: []
        };
    }

    async UNSAFE_componentWillMount() {
        let _this = this;
        if (Platform.OS === 'android') {
            PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.READ_CALL_LOG,
                {
                    'title': 'Call Log',
                    'message': 'Access your call logs'
                }
            ).then(() => {
                _this.loadCallRecords();
            })
        } else {
            _this.loadCallRecords();
        }
    }

    loadCallRecords() {
        let _this = this;
        try {
            CallLogs.getAll({limit:5,isDistinct:true}).then(c => {
                _this.setState({list: c})
            });
        }
        catch (e) {
            console.log(e);
        }
    }

    render() {
        return (
            <SafeAreaView style={styles.container}>
                <Text style={styles.welcome}>Welcome to React Native!</Text>
                <ScrollView style={{flex: 1}}>
                    <Text style={styles.instructions}>
                        {JSON.stringify(this.state.list, null, '\t')}
                    </Text>
                </ScrollView>
            </SafeAreaView>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
