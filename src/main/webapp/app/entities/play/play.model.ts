import * as dayjs from 'dayjs';
import { IResposta } from 'app/entities/resposta/resposta.model';
import { IParticipante } from 'app/entities/participante/participante.model';
import { IEdicao } from 'app/entities/edicao/edicao.model';

export interface IPlay {
  id?: number;
  playData?: dayjs.Dayjs | null;
  respostas?: IResposta[] | null;
  participantes?: IParticipante[] | null;
  edicao?: IEdicao | null;
}

export class Play implements IPlay {
  constructor(
    public id?: number,
    public playData?: dayjs.Dayjs | null,
    public respostas?: IResposta[] | null,
    public participantes?: IParticipante[] | null,
    public edicao?: IEdicao | null
  ) {}
}

export function getPlayIdentifier(play: IPlay): number | undefined {
  return play.id;
}
