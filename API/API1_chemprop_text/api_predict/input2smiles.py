import codecs
import datetime
import logging

from rdkit.Chem import MolToSmiles, SDMolSupplier

from config import errors

logging.basicConfig(level=logging.INFO)

now = datetime.datetime.now()


# input으로 sdf file을 받은 경우 이를 smiles string으로 변환하는 코드
# to_smiles에서 씀. 남기기 0404


def sdf2smiles(sdf_path):
    mols = [mol for mol in SDMolSupplier(sdf_path)]
    mol1 = mols[0]
    smiles = MolToSmiles(mol1)
    return smiles


def to_smiles(request, params):
    """

    :param request:
    :param params:
    :return:  ['smiles'] or 004 chemical name, 006 sdf file 형식, 003 type 입력 에러
    """
    input_type = request['type']
    input_value = request['value']
    name2smiles = params['chemical_name_smiles_dict']

    # type = chemical
    if input_type == 'chemical':
        # 화학명 대소문자 구분을 위해 대문자로 들어온경우 모두 소문자로 변경하여 값 비교
        name_lower = input_value.lower()
        try:
            if name_lower in name2smiles:
                smiles = name2smiles[name_lower]
                logging.info('In chemical.csv')
                return smiles
                # 리스트 처리를 해주면 rdkit lipinski module에서 스마일스를 인식 못한다.
        except SyntaxError:
            url = f'https://pubchem.ncbi.nlm.nih.gov/#query={name_lower}'
            logging.info(f'You can find Isomeric SMILES in this website : {url}, otherwise '
                         f'specify input value by changing input type')
            return errors('004'),

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
            smiles = sdf2smiles(sdf_path)
            return smiles
        else:
            return errors('006')
    else:
        return errors('003')


def sdf_hex2smiles2(sdf_hex):
    """
    sdf binary decoding???? 하는 법??? - 추후반영
    :param sdf_hex:
    :return:
    """
    # decoding : hex -> utf-8 text
    utf8_value = codecs.decode(sdf_hex, 'hex')
    utf8_str = str(utf8_value, 'utf-8')

    # utf-8 text to file
    sdf_path = 'test_sdf.sdf'
    with open(sdf_path, 'w') as f:
        f.write(utf8_str)

    # file -> smiles
    mols = [mol for mol in SDMolSupplier(sdf_path)]
    mol1 = mols[0]
    smiles = MolToSmiles(mol1)
    return smiles


def to_smiles2(request):
    """

    :param request: {'type':'sdf','value':'sdf binary code'}
                    {'type':'smiles','value':'CCC(=O)OC1(C(CC2C1(CC(C3(C2CCC4=CC(=O)C=CC43C)F)O)C)C)C(=O)CCl'}
    :return:  ['smiles'] or 004 chemical name, 006 sdf file 형식, 003 type 입력 에러
    """
    input_type = request['type']
    input_value = request['value']

    try:
        # type = smiles
        if input_type == 'smiles':
            # errors("005") => 학습, 예측시 invalid smiles 가 아닌 이상 필요 없음.
            smiles = input_value
            return smiles
        # type = sdf
        elif input_type == 'sdf':
            sdf_hex = input_value
            smiles = sdf_hex2smiles2(sdf_hex)
            return smiles
    except Exception:
        # return errors('003') # type 셋 중 하나 입력
        return errors('006')
    # 에러코드 -sdf 형식을 다시 맞춰주세요?


if __name__ == "__main__":
    ret = to_smiles2(request = {'type':'smiles','value':'CCC(=O)OC1(C(CC2C1(CC(C3(C2CCC4=CC(=O)C=CC43C)F)O)C)C)C(=O)CCl'} )
    print(ret)