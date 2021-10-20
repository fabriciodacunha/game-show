import { IResposta } from 'app/entities/resposta/resposta.model';
import { IEdicao } from 'app/entities/edicao/edicao.model';
import { Assunto } from 'app/entities/enumerations/assunto.model';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';

export interface IQuestao {
  id?: number;
  questaoTitulo?: string | null;
  alternativaA?: string | null;
  alternativaB?: string | null;
  alternativaC?: string | null;
  alternativaD?: string | null;
  nivelIdade?: number | null;
  assunto?: Assunto | null;
  alternativaCerta?: Alternativa | null;
  respostas?: IResposta[] | null;
  edicao?: IEdicao | null;
}

export class Questao implements IQuestao {
  constructor(
    public id?: number,
    public questaoTitulo?: string | null,
    public alternativaA?: string | null,
    public alternativaB?: string | null,
    public alternativaC?: string | null,
    public alternativaD?: string | null,
    public nivelIdade?: number | null,
    public assunto?: Assunto | null,
    public alternativaCerta?: Alternativa | null,
    public respostas?: IResposta[] | null,
    public edicao?: IEdicao | null
  ) {}
}

export function getQuestaoIdentifier(questao: IQuestao): number | undefined {
  return questao.id;
}
