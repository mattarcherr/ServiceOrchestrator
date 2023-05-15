#include "rest.h"
//#include <QQmlComponent>

#include <QDebug>
//#include <QQmlProperty>

REST::REST()
{
    userID = genID();
}

// *********** rest services ***********

int REST::genID()
{
    get("http://localhost:8080/genID");

    return replyObject["id"].toInt();
}

QVariant REST::queryTrips()
{
    get("http://localhost:8080/trip/query");

    return replyObject["trips"];
}

QVariant REST::queryTripsParams(QString location)
{
    qInfo() << "http://localhost:8080/trip/query/param?location='"+location+"'";
    get("http://localhost:8080/trip/query/param?location="+location+"'");

    return replyObject["trips"];
}

void REST::proposeTrip(QDate date, QString title, QGeoCoordinate coord, QString location)
{
    // get trip ID
    int tripID = genID();

    //format date string
    QString formattedDate;
    formattedDate =
            QString().number(date.year()) + "-" +
            QString().number(date.month()).rightJustified(2,'0') + "-" +
            QString().number(date.day()).rightJustified(2,'0');

    //create json object
    QJsonObject json;
    json.insert("userID", userID);
    json.insert("tripID", tripID);
    json.insert("title", title);
    json.insert("location", location);
    json.insert("latitude", coord.latitude());
    json.insert("longitude", coord.longitude());
    json.insert("date", formattedDate);

    // post json
    post("http://localhost:8080/trip/propose", QJsonDocument(json));
}

void REST::expressTripInterest(int tripID)
{
    //create json object
    QJsonObject json;
    json.insert("userID", userID);
    json.insert("tripID", tripID);

    // post json
    post("http://localhost:8080/interest/express", QJsonDocument(json));
}

QVariant REST::queryTripInterest(int tripID)
{
    get("http://localhost:8080/interest/query?tripID="+QString::number(tripID));

    return replyObject["interest"];
}

// *********** QNetwork function ***********

void REST::get(QString addr)
{
    QNetworkReply* reply = manager.get(QNetworkRequest(QUrl(addr)));
    connect(reply, &QNetworkReply::readyRead, this, &REST::readyRead);

    // wait for finished signal
    QEventLoop loop;
    connect(reply, &QNetworkReply::finished, &loop, &QEventLoop::quit);
    loop.exec();
}

void REST::post(QString addr, QJsonDocument data)
{
    QNetworkRequest request = QNetworkRequest(QUrl(addr));
    request.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");


    QNetworkReply* reply = manager.post(request, data.toJson());
    connect(reply, &QNetworkReply::readyRead, this, &REST::readyRead);

    // wait for finished signal
    QEventLoop loop;
    connect(reply, &QNetworkReply::finished, &loop, &QEventLoop::quit);
    loop.exec();
}

void REST::readyRead()
{
    QNetworkReply *reply = qobject_cast<QNetworkReply*>(sender());
    if (reply) {
        // decipher http-get reply
        QByteArray replyBtytes = reply->readAll();
        QJsonDocument jsonDoc = QJsonDocument::fromJson(replyBtytes);
        QJsonObject jsonObj=jsonDoc.object();

        replyObject = jsonObj.toVariantMap();
    }
}
