#ifndef REST_H
#define REST_H

#include <QObject>

#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkReply>
#include <QtNetwork/QNetworkRequest>

#include <QEventLoop>

#include <QJsonDocument>
#include <QJsonObject>

#include <QGeoCoordinate>

class REST : public QObject
{
    Q_OBJECT

public:
    REST();

public slots:

    Q_INVOKABLE QVariant queryTrips();
    Q_INVOKABLE QVariant queryTripsParams(QString);
    Q_INVOKABLE void proposeTrip(QDate, QString, QGeoCoordinate, QString);
    Q_INVOKABLE void expressTripInterest(int tripID);
    Q_INVOKABLE QVariant queryTripInterest(int tripID);

private slots:
    void get(QString location);
    void post(QString, QJsonDocument);
//    void finishsed(QNetworkReply *reply);
    void readyRead();

private:
    int userID;
    int genID();
    QNetworkAccessManager manager;
    QVariantMap replyObject;

};

#endif // REST_H
