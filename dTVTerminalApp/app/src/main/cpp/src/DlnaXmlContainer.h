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

    namespace dtvt {

        using namespace std;

        /**
         * 機能：Containerとするクラスである。Dlnaクラスから使う。
         */
        class DlnaXmlContainer {
        public:
            DlnaXmlContainer();
            virtual ~DlnaXmlContainer();

            inline void setMsgId(DLNA_MSG_ID id) { mDLNA_MSG_ID = id;  }
            inline const DLNA_MSG_ID getMsgId() { return mDLNA_MSG_ID; }

            void addXml(std::string itemId, du_uchar* xml);
            void addVVectorString(VVectorString & vs);

            inline const VVectorString& getAllVVectorString() {  return mVVectorString; }

            bool getXml(std::string itemId, du_uchar** outXml);

            void cleanAll();

        private:
            void uninit();

        private:
            DLNA_MSG_ID mDLNA_MSG_ID;
            VVectorString mVVectorString;
            std::map<std::string, du_uchar*> mXmls;
        };

    } //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif //DTVTERMINALAPP_DLNAXMLCONTAINER_H
