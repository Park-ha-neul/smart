import csv
from urllib.request import urlopen
from urllib.parse import quote
import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By

from config import errors, InitSvcConfig


def cir_convert(ids):
    try:
        url = 'http://cactus.nci.nih.gov/chemical/structure/' + quote(ids) + '/smiles'
        ans = urlopen(url).read().decode('utf8')
        return ans
    except:
        return 'Did not work'


identifiers = ['3-Methylheptane', 'Aspirin', 'Diethylsulfate', 'Diethyl sulfate', '50-78-2', 'Adamant']


# for ids in identifiers:
#     print(ids, cir_convert(ids))


def pubchem_smiles_bs(molecule_name):
    # pubchem이 javascript로 동적 페이지로 이루어져 있어 beautifulsoup은 먹히지 않음.
    url = f'https://pubchem.ncbi.nlm.nih.gov/#query={molecule_name}'
    print(url)
    response = requests.get(url)
    if response.status_code == 200:
        html = response.text
        soup = BeautifulSoup(html, 'html.parser')
        isomeric_breakword = soup.select_one('#featured-results > div > div.box-shadow > div > '
                                             'div.p-md-rectangle.flex-container.flex-nowrap.width-100 > '
                                             'div.flex-grow-1.p-md-left > div:nth-child(5) > div > span > '
                                             'span.breakword')
        isomeric_smiles = soup.select_one('#featured-results > div > div.box-shadow > div > '
                                          'div.p-md-rectangle.flex-container.flex-nowrap.width-100 > '
                                          'div.flex-grow-1.p-md-left > div:nth-child(5) > div > span > span.breakword '
                                          '> span')
        print(f'break word: {isomeric_breakword}')
        # print(f'break word text {isomeric_smiles.get_text()}')
        print(f'smiles: {isomeric_smiles}')

        print('yes')
    else:
        print(response.status_code)
        print('no')


def smiles_pubchem(molecule_name):
    chromeOptions = webdriver.ChromeOptions()
    chromeOptions.add_argument("--headless")
    chromeOptions.add_argument("--remote-debugging-port=9222")
    chromeOptions.add_argument('--no-sandbox')
    driver = webdriver.Chrome(options=chromeOptions)
    url = f'https://pubchem.ncbi.nlm.nih.gov/#query={molecule_name}'
    driver.get(url)

    driver.implicitly_wait(5)
    value = '//*[@id="featured-results"]/div/div[2]/div/div[1]/div[2]/div[5]/div/span/span[2]/span'

    iso = driver.find_element(by=By.XPATH, value=value)
    print(iso)
    return iso.text


def to_smiles_by_name(name):
    with open(r'C:\Users\coco7\PycharmProjects\pharmai\platform\API1\API1_chemprop_onemodel_selenium\api_predict\chemical.csv',
              mode='r') as inp:
        reader = csv.reader(inp)
        dict_from_csv: dict = {rows[1]: rows[2] for rows in reader}
    try:
        if name in dict_from_csv:
            smiles = dict_from_csv[name]
            print('in chemical.csv')
            return smiles
        else:
            smiles = smiles_pubchem(name)
            print('In pubchem')
            return smiles

    except:
        print('Not implemented')
        return errors('004')

def for_example():
    novel_drugs = 'lutetium (177Lu) vipivotide tetraxetan,nivolumab and relatlimab-rmbw,ganaxolone,pacritinib,' \
                  'mitapivat,sutimlimab-jome,faricimab-svoa,tebentafusp-tebn,abrocitinib,daridorexant'.split(',')
    smi_lst ={}
    for i in novel_drugs:
        print('-' * 20)
        smiles = to_smiles_by_name(i)
        print(f'NAME : {i}, \n SMILES : {smiles}')
        smi_lst[i] =smiles
    print(smi_lst)
    print('The End')

if __name__ == "__main__":
    novel_drugs = 'lutetium (177Lu) vipivotide tetraxetan,nivolumab and relatlimab-rmbw,ganaxolone,pacritinib,' \
                  'mitapivat,sutimlimab-jome,faricimab-svoa,tebentafusp-tebn,abrocitinib,daridorexant'.split(',')
    smi_lst = {}
    for i in novel_drugs:
        print('-' * 20)
        print(i)
        smiles = to_smiles_by_name(i)
        print(f'NAME : {i}, \n SMILES : {smiles}')
        smi_lst[i] = smiles
    print(smi_lst)
    print('The End')


# 화면 안띄우고 background로 하는 것? 디스플레이 할 수 없다고 에러 날 것.