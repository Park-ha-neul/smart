from response import Molecule
from config import errors
from urllib.request import urlopen
from urllib.parse import quote

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
        if name_lower in name2smiles:
            smiles = name2smiles[name_lower]
            return smiles # smiles는 리스트 안에 있어야 온전하게 전체를 예측할 수 있다.
            # 리스트 처리를 해주면 리핀스키에서 스마일스를 인식 못한다.
        else:
            try: # 웹크롤링으로 이름-smiles 가져오기 - pubchem에서 가져오는 코드 ??
                url = 'http://cactus.nci.nih.gov/chemical/structure/' + quote(name_lower) + '/smiles'
                ans = urlopen(url).read().decode('utf8')
                return ans
            except:
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
