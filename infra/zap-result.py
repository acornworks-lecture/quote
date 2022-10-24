import json
import os
import sys

ALERTS_KEY = 'alerts'
SITE_KEY = 'site'
RISK_CODE_KEY = 'riskcode'
CONFIDENCE_KEY = 'confidence'
RISKDESC_KEY = 'riskdesc'
ALERT_KEY = 'alert'

def scan_result(file_name, riskcode, confidence = 2):
    if not os.path.exists(file_name):
        print("FILE_NOT_EXIST: %s" % file_name)
        sys.exit(1)
    
    with open(file_name) as file_obj:
        result_obj = json.load(file_obj)

        if SITE_KEY not in result_obj:
            print("KEY_%s_NOT_EXIST" % SITE_KEY)
            sys.exit(1)
        
        site_list = result_obj[SITE_KEY]
        exit_code = 0

        for site_obj in site_list:
            if ALERTS_KEY in site_obj:
                alert_list = site_obj[ALERTS_KEY]

                filtered_list = list(filter(
                    lambda x: (int(x[RISK_CODE_KEY]) >= riskcode) and (int(x[CONFIDENCE_KEY]) >= confidence), alert_list))

                if len(filtered_list) > 0:
                    for alert_obj in filtered_list:
                        print("[%s] %s" % (alert_obj[RISKDESC_KEY], alert_obj[ALERT_KEY]))
                    
                    exit_code = 1
        
        sys.exit(exit_code)

if __name__ == '__main__':
    argv_len = len(sys.argv)

    if argv_len >= 4:
        scan_result(sys.argv[1], int(sys.argv[2]), int(sys.argv[3]))
    elif len(sys.argv) >= 3:
        scan_result(sys.argv[1], int(sys.argv[2]))        
    else:
        print('Usage: zap-result.py <JSON result file> <threshhold riskcode> <threshold confidence level, optional>')

    

        