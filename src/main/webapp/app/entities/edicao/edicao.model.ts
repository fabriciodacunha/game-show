import * as dayjs from 'dayjs';
import { IPlay } from 'app/entities/play/play.model';
import { IQuestao } from 'app/entities/questao/questao.model';

export interface IEdicao {
  id?: number;
  edicaoTitulo?: string | null;
  edicaoData?: dayjs.Dayjs | null;
  play?: IPlay | null;
  questaos?: IQuestao[] | null;
}

export class Edicao implements IEdicao {
  constructor(
    public id?: number,
    public edicaoTitulo?: string | null,
    public edicaoData?: dayjs.Dayjs | null,
    public play?: IPlay | null,
    public questaos?: IQuestao[] | null
  ) {}
}

export function getEdicaoIdentifier(edicao: IEdicao): number | undefined {
  return edicao.id;
}
