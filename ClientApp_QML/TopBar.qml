import QtQuick 2.12
import QtQuick.Controls 2.15

Rectangle {
    id: topBar
    color: "#4c4c4c"
    anchors {
        top: parent.top
        left: parent.left
        right: parent.right
    }
    height: 30

    Rectangle {
        id: queryRect

        HoverHandler {
            id: mouse
            acceptedDevices: PointerDevice.Mouse
        }

        anchors {
            top:parent.top
            bottom: parent.bottom
            left: parent.left
        }

        width: 75

        color: mouse.hovered ? "#686868" : "#4c4c4c"
        Text {
            id: queryText
            text: qsTr("Query Trip")
            color: "#f1f1f1"

            anchors.centerIn: parent
        }

        MouseArea {
            anchors.fill: parent
            onClicked: mainLoader.source = "queryTripWindow.qml"
        }
    }
    Rectangle {
        anchors {
            top:parent.top
            bottom: parent.bottom
            left: queryRect.right
        }

        HoverHandler {
            id: mouse2
            acceptedDevices: PointerDevice.Mouse
        }

        width: 75
        color: mouse2.hovered ? "#686868" : "#4c4c4c"
        Text {
            id: proposeText
            text: qsTr("Propose Trip")
            color: "#f1f1f1"

            anchors.centerIn: parent
        }

        MouseArea {
            anchors.fill: parent
            onClicked: mainLoader.source = "proposeTripWindow.qml"
        }

    }
}
