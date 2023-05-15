import QtQuick 2.0
import QtQuick.Window 2.14
import QtQuick.Controls 1.4
import QtPositioning 5.12
import QtLocation 5.12

Rectangle {
    color: "#2c2c2c"

    property var date;
    property var title;
    property var location;
    property var position;

    StackView {
        id: stack
        initialItem: titleView
        anchors.fill: parent
    }

    Plugin {
        id: mapPlugin
        name: "osm"
    }

    Component {
        id: titleView
        Item {

            Text {
                anchors.horizontalCenter: parent.horizontalCenter
                text: "
                        To propose a new trip several parameters are needed.
                        These include the trip title and given location name
                        aswell as the exact map coordinates and the starting date.

                        To select the coordinates simply position the map center in the
                        correct position then click next.
                        Similarly the start date can be selected from any date in the future
                        however, it should be noted that weather data is only avaliable
                        up to 7 days from the current date and so any date chosen past this
                        date will not contain weather data."
                color: "white"
            }
            TextField {
                id: titleField
                anchors.centerIn: parent
                placeholderText: "Enter title"
            }
            TextField {
                id: locationField
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: titleField.bottom
                placeholderText: "Enter location"
            }

            Button {
                text: "next"
                height: 30; width: 50
                anchors {
                    right: parent.right
                    bottom: parent.bottom
                }
                onClicked: func => {
                               title = titleField.text
                               location = locationField.text

                               titleField.text = ""
                               locationField.text = ""
                               stack.push(mapView)
                           }
            }
        }
    }

    Component {
        id: mapView
        Item {
            Map {
                id: map
                anchors.fill: parent
                plugin: mapPlugin
                center: QtPositioning.coordinate(51.51, -0.13)
                zoomLevel: 5
            }

            Button {
                text: "back"
                height: 30; width: 50
                anchors {
                    left: parent.left
                    bottom: parent.bottom
                }
                onClicked: stack.pop(titleView)
            }

            Button {
                text: "next"
                height: 30; width: 50
                anchors {
                    right: parent.right
                    bottom: parent.bottom
                }
                onClicked: func => {
                               position = map.center
                               stack.push(calendarView)
                           }
            }
        }
    }
    Component {
        id: calendarView
        Item {
            Calendar {
                id: calendar
                anchors.centerIn: parent
                minimumDate: new Date()
            }

            Button {
                text: "back"
                height: 30; width: 50
                anchors {
                    left: parent.left
                    bottom: parent.bottom
                }
                onClicked: stack.pop(mapView)
            }

            Button {
                text: "Finish"
                height: 30; width: 50
                anchors {
                    right: parent.right
                    bottom: parent.bottom
                }
                onClicked: func => {
                               date = calendar.selectedDate
                               sendProposal()
                           }
            }
        }
    }

    function sendProposal(){
        REST.proposeTrip(date, title, position, location)
        stack.pop(null)
    }
}


