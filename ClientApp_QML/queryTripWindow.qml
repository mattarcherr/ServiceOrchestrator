import QtQuick 2.15
import QtQuick.Layouts 1.15
import QtQuick.Controls 2.15
import QtPositioning 5.12
import QtLocation 5.12


Rectangle {
    color: "#2c2c2c"

    ListModel {
        id: tripsModel
    }

    Plugin {
        id: mapPlugin
        name: "osm"
    }

    Rectangle {
        id: queryBar
        color: "#2c2c2c"
        anchors {
            top: parent.top
            left: parent.left
            right: parent.right
        }
        height: 80
        Label {
            text: "Enter the location you're looking for,\notherwise leave the field blank to list \nall trips"
            color: "white"
        }
        TextField {
            id: titleField
            anchors {
                horizontalCenter: parent.horizontalCenter
                top: parent.top
            }

            placeholderText: "Enter location"
        }
        Button {
            onClicked: func => {
                           if (titleField.text !== "") {
                               console.log(titleField.text)
                               getTripsParam(titleField.text)
                           }
                           else { getTrips() }
                       }
            anchors.right: parent.right
            text: "Query"
        }
    }

    Text {
        id: noResultsText
        text: "NO RESULTS FOUND"
        color: "white"
        font.pixelSize: 20
        visible: false
        anchors.centerIn: parent
    }

    ScrollView {
        id: scrollView
        anchors {
            top: queryBar.bottom
            bottom: parent.bottom
            left: parent.left
            right: parent.right
        }
        ScrollBar.horizontal.policy: ScrollBar.AlwaysOff

        ListView {
            anchors.fill: parent
            model: tripsModel
            spacing: 150
            delegate: Rectangle {
                anchors.horizontalCenter: parent.horizontalCenter
                width: parent.width / 2; height: width * 0.8; color: "#2c2c2c"
                ColumnLayout {
                    anchors.centerIn: parent
                    width: parent.width
                    Label {
                        id: label
                        text: title + " - " + location
                        color: "white"
                        Layout.alignment: Qt.AlignHCenter
                    }
                    Map {
                        id: map
                        Layout.minimumHeight: 120; Layout.fillWidth: true

                        plugin: mapPlugin
                        center: QtPositioning.coordinate(longitude, latitude)
                        zoomLevel: 5
                        enabled: false
                    }
                    Label {
                        id: bottomLabel
                        color: "white"
                        text: "By user: " + userID + "\nTrip number: " + tripID + "\nStarting date: " + date

                        Layout.preferredWidth: 120
                        Layout.preferredHeight: 35
                        Layout.alignment: Qt.AlignLeft
                     }
                    Text {
                        id: interestText
                        text: interest + " interested"
                        color: "white"
                        font.underline: true
                        MouseArea {
                            anchors.fill: parent
                            hoverEnabled: true
                            onEntered: cursorShape = Qt.PointingHandCursor
                            onClicked: func => {
                                           var interest = getInterest(tripID)
                                           var i = 0;
                                           menu.clear()
                                           while(interest[i] !== undefined)
                                           {
                                               menu.addEntry(interest[i]["id"])
                                               i++
                                           }
                                           menu.open()
                                       }
                            Menu {
                                id: menu
                                y: interestText.height
                                MenuItem {
                                    text: "Users interested: "
                                }

                                function addEntry(title) {
                                    menu.addItem(menuItem.createObject(menu, { text: title }))
                                }
                                function clear() {
                                    var count = menu.count
                                    for (var i = 0; i < count; i++)
                                    {
                                        menu.removeItem(menu.itemAt(1))
                                    }
                                }
                                Component {
                                    id: menuItem
                                    MenuItem {}
                                }

                            }

                    }}
                    CheckBox {
                        id: control
                        enabled: true
                        Text {
                            text: "\tClick to show interest"
                            color: "white"
                        }

                        indicator: Rectangle {
                            implicitWidth: 16; implicitHeight: 16; radius: 3
                            border.color: control.down ? "#ffffff" : "#21be2b"

                            Rectangle {
                                width: 16; height: 16; radius: 2
                                color: control.down ? "#ffffff" : "#21be2b"
                                visible: control.checked
                            }
                        }

                        Layout.preferredWidth: 120; Layout.preferredHeight: 35
                        Layout.alignment: Qt.AlignCenter
                        LayoutMirroring.enabled: true

                        onClicked: func => {
                                       enabled = false
                                       interest++
                                       REST.expressTripInterest(tripID)
                                   }
                    }
                    Image {
                        id: name
                        source: "https://www.7timer.info/bin/civillight.php?lon="+longitude+"&lat="+latitude+"&lang=en&ac=0&unit=metric&tzshift=0"
                        Layout.preferredHeight: parent.width / 4
                        Layout.preferredWidth: parent.width
                        Component.onCompleted: func => {
                                                   if (weather == undefined) {
                                                       visible = false
                                                       console.log("undefined")
                                                   }
                                                   if (weather == null) { console.log("null") }
                                               }
                    }
                }
            }
        }
    }
    function getTrips(){
        noResultsText.visible = false
        tripsModel.clear()
        var trips = REST.queryTrips()
        var i = 0;
        while(trips[i] !== undefined)
        {
            tripsModel.append(trips[i])
            i++
        }
        if (trips[0] === undefined) {
            noResultsText.visible = true
        }
    }
    function getTripsParam(title){
        noResultsText.visible = false
        tripsModel.clear()
        var trips = REST.queryTripsParams(title)
        var i = 0;
        while(trips[i] !== undefined)
        {
            tripsModel.append(trips[i])
            i++
        }
        if (trips[0] === undefined) {
            noResultsText.visible = true
        }
    }
    function getInterest(tripID){ return REST.queryTripInterest(tripID) }
}
