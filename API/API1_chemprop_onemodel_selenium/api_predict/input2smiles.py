from selenium import webdriver
from selenium.webdriver.common.by import By

from response import Molecule
from config import errors

import logging
logging.basicConfig(level=logging.INFO)



def smiles_pubchem(molecule_name):
    chromeOptions = webdriver.ChromeOptions()
    chromeOptions.add_argument("--headless")
    chromeOptions.add_argument("--remote-debugging-port=9222")
    chromeOptions.add_argument('--no-sandbox')
    chromeOptions.set_capability('browserless:token','YOUR-API-TOKEN')
    driver = webdriver.Remote(
        command_executor='https://chrome.browserless.io/webdriver',
        desired_capabilities=chromeOptions.to_capabilities()
    )
    driver = webdriver.Chrome(chrome_options=chromeOptions)
    url = f'https://pubchem.ncbi.nlm.nih.gov/#query={molecule_name}'
    driver.get(url)

    driver.implicitly_wait(10)
    value = '//*[@id="featured-results"]/div/div[2]/div/div[1]/div[2]/div[5]/div/span/span[2]/span'
    iso = driver.find_element(by=By.XPATH, value=value)
    return iso.text


def to_smiles(value, params):
    """

    :param value:
    :param params:
    :return:  ['smiles'] or 004 chemical name, 006 sdf file 형식, 003 type 입력 에러
    """
    input_type = value['type']
    input_value = value['value']
    # input_path = value['path']
    name2smiles = params['chemical_name_smiles_dict']

    # type = chemical
    if input_type == 'chemical':
        # 화학명 대소문자 구분을 위해 대문자로 들어온경우 모두 소문자로 변경하여 값 비교
        name_lower = input_value.lower()
        try:
            if name_lower in name2smiles:
                smiles = name2smiles[name_lower]
                print('In chemical.csv')
                return smiles
                # 리스트 처리를 해주면 리핀스키에서 스마일스를 인식 못한다.
            else:
                # 웹크롤링으로 이름-smiles 가져오기 - pubchem에서 가져오는 코드
                smiles = smiles_pubchem(name_lower)
                print('In pubchem')
                return smiles
        except SyntaxError:
            print('Not Implemented')
            return errors('004')

    # type = smiles
    elif input_type == 'smiles':
        # errors("005") => 학습, 예측시 invalid smiles 가 아닌 이상 필요 없음.
        smiles = input_value
        return smiles

    # type = sdf
    elif input_type == 'sdf':
        # 6)  file형식이 sdf가 아닌 경우
        if input_value.endswith('.sdf'):
            sdf_path = input_value
            smiles = Molecule.sdf2smiles(sdf_path)
            return smiles
        else:
            errors('006')
    else:
        return errors('003')
