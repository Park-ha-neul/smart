from rdkit.Chem import SDMolSupplier, MolToSmiles
import codecs

# UI : sdf -> hex (binary data)
# API : hex -> sdf -> mol -> smiles


def ui_sdf2hex(sdf_path):
    bin_data = open(sdf_path, 'rb').read()
    hex_data = codecs.encode(bin_data, "hex_codec")
    return hex_data


def hex2sdf2smiles(hex_data):
    aschii_value = codecs.decode(hex_data, 'hex')
    aschii_str = str(aschii_value, 'utf-8')
    with open('test_sdf.sdf', 'w') as f:
        f.write(aschii_str)
    smiles = sdf2smiles('test_sdf.sdf')
    return smiles


def sdf2smiles(sdf_path):
    mols = [mol for mol in SDMolSupplier(sdf_path)]
    # print(f'================MOLS {mols}===========================')
    mol1 = mols[0]
    smiles = MolToSmiles(mol1)
    return smiles


def sdf2smiles2(sdf_path):
    # 몇개나 smiles가 안에 있나 확인용.
    mols = [mol for mol in SDMolSupplier(sdf_path)]
    for num,mol in enumerate(mols):
        smiles = MolToSmiles(mol)
        print(f'{num, smiles}')
        # print(mol.GetProp("PUBCHEM_IUPAC_CAS_NAME"))


if __name__=="__main__":
    sdf_path = './Caffeine.sdf'
    hex = ui_sdf2hex(sdf_path)
    smiles = hex2sdf2smiles(hex)

    print(smiles)



