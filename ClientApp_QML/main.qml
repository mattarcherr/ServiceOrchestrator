import QtQuick 2.12
import QtQuick.Window 2.12
import QtQuick.Controls 2.15

Window {
    id: root
    width: 640
    height: 480
    visible: true
    title: qsTr("AAD Client Application")



    TopBar {
        id: topBar
    }

//    CheckBox {
//        Text {
//            text: "test123"
//            color: "blue"
//        }

////        contentItem: Text {
////            color: "blue"
////        }
//    }

    Loader {
        id: mainLoader

        source: "queryTripWindow.qml"

        anchors {
            left: parent.left
            right: parent.right
            top: topBar.bottom
            bottom: parent.bottom
        }
    }
}
