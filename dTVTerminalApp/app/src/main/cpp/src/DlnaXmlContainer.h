/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVTERMINALAPP_DLNAXMLCONTAINER_H
#define DTVTERMINALAPP_DLNAXMLCONTAINER_H

#include <string>
#include <vector>
#include "Common.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */
using namespace std;
typedef std::vector<std::string> VectorString;
typedef std::vector<VectorString> VVectorString;
/**
 * 機能：Containerとするクラスである。Dlnaクラスから使う。
 */
class DlnaXmlContainer {
public:
    DlnaXmlContainer();
    virtual ~DlnaXmlContainer();
    const char * const RecVideoXml_Item_Begin_Tag ="<item id=\"";
    const char * const RecVideoXml_Item_End_Tag ="</item>";
    void addXml(du_uchar* xml, size_t size);
    void addVVectorString(VVectorString & vs);

    void getDidlLiteDocHead(du_uchar* allRecordedVideoXml, std::string& outStr);
    void getDidlLiteDocTail(du_uchar* allRecordedVideoXml, std::string& outStr);
    bool getItemStringByItemId(du_uchar* allRecordedVideoXml, std::string itemId, du_uchar** outXmlStr);

    inline const VVectorString& getAllVVectorString() {  return mVVectorString; }

    bool getXml(std::string itemId, du_uchar** outXml);

    void cleanAll();

private:
    void uninit();

private:
    VVectorString mVVectorString;
    std::vector<du_uchar*> mXmls;
};

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif //DTVTERMINALAPP_DLNAXMLCONTAINER_H
